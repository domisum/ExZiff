package de.domisum.exziff.world;

import lombok.Getter;
import lombok.Setter;

public class World
{

	// CONSTANTS
	private static final int CHUNK_CLUSTER_RADIUS = 32;

	// DATA
	private ChunkClusterField clusterField = new ChunkClusterField(CHUNK_CLUSTER_RADIUS);

	// REFERENCES
	private ChunkClusterLoaderSaver chunkClusterLoaderSaver;

	// SETTINGS
	@Getter @Setter private int maximumLoadedChunkClusters = 128;


	// INIT
	public World(ChunkClusterLoaderSaver chunkClusterLoaderSaver)
	{
		this.chunkClusterLoaderSaver = chunkClusterLoaderSaver;
	}


	// GETTERS
	public short getMaterialId(int x, int y, int z)
	{
		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		return chunk.getMaterialId(icX, y, icZ);
	}

	public short getMaterialSubId(int x, int y, int z)
	{
		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		return chunk.getMaterialSubId(icX, y, icZ);
	}


	// SETTERS
	public void setMaterialIdAndSubId(int x, int y, int z, byte materialId, byte materialSubId)
	{
		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		chunk.setMaterialIdAndSubId(icX, y, icZ, materialId, materialSubId);
	}


	// CHUNK AND CLUSTER
	private Chunk getChunkAt(int x, int z)
	{
		int cX = getChunkXorZ(x);
		int cZ = getChunkXorZ(z);

		int clX = getChunkClusterXorZ(cX);
		int clZ = getChunkClusterXorZ(cZ);
		ChunkCluster chunkCluster = getChunkCluster(clX, clZ);

		int iclX = getInChunkClusterXorZ(cX);
		int iclZ = getInChunkClusterXorZ(cZ);
		Chunk chunk = chunkCluster.getChunk(iclX, iclZ);
		return chunk;
	}

	private ChunkCluster getChunkCluster(int clX, int clZ)
	{
		ChunkCluster chunkCluster = this.clusterField.getCluster(clX, clZ, true);

		// chunk cluster not loaded, loading it
		if(chunkCluster == null)
			chunkCluster = loadChunkCluster(clX, clZ);

		return chunkCluster;
	}


	// LOADING/SAVING
	private ChunkCluster loadChunkCluster(int clX, int clZ)
	{
		System.out.println("load chunk cluster: "+clX+" "+clZ);

		// if number of currently loaded chunk clusters is too high, unload
		if(this.clusterField.getClusterList().size() >= this.maximumLoadedChunkClusters)
			unloadLongestUnusedChunkCluster();

		ChunkCluster chunkCluster = this.chunkClusterLoaderSaver.loadChunkCluster(clX, clZ);

		// chunk cluster not saved to disk, create new chunk cluster
		if(chunkCluster == null)
			chunkCluster = new ChunkCluster(clX, clZ);

		this.clusterField.addCluster(chunkCluster);
		return chunkCluster;
	}

	private void unloadLongestUnusedChunkCluster()
	{
		ChunkCluster longestUnusedChunkCluster = this.clusterField.getLongestUnusedChunkCluster();

		System.out.println("unloacChunkCluster: "+longestUnusedChunkCluster);

		this.chunkClusterLoaderSaver.saveChunkCluster(longestUnusedChunkCluster);
		this.clusterField.removeCluster(longestUnusedChunkCluster);

		System.gc();
	}

	public void save()
	{
		for(ChunkCluster cluster : this.clusterField.getClusterList())
			this.chunkClusterLoaderSaver.saveChunkCluster(cluster);
	}


	// INTERNAL CALCULATIONS
	private static int getChunkClusterXorZ(int cXorZ)
	{
		return getContainerXorZ(cXorZ, ChunkCluster.WIDTH);
	}

	private static int getInChunkClusterXorZ(int cXorZ)
	{
		return getInContainerXorZ(cXorZ, ChunkCluster.WIDTH);
	}

	private static int getChunkXorZ(int xOrZ)
	{
		return getContainerXorZ(xOrZ, Chunk.WIDTH);
	}

	private static int getInChunkXOrZ(int xOrZ)
	{
		return getInContainerXorZ(xOrZ, Chunk.WIDTH);
	}

	private static int getContainerXorZ(int elementXorZ, int containerWidth)
	{
		if(elementXorZ < 0)
			return (elementXorZ+1)/containerWidth-1;

		return elementXorZ/containerWidth;
	}

	private static int getInContainerXorZ(int elementXorZ, int containerWidth)
	{
		if(elementXorZ < 0)
			return (elementXorZ%containerWidth)+containerWidth;

		return elementXorZ%containerWidth;
	}

}
