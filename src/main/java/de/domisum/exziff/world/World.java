package de.domisum.exziff.world;

import de.domisum.exziff.world.loadersaver.ChunkClusterSource;
import lombok.Getter;
import lombok.Setter;

public class World
{

	// CONSTANTS
	private static final int CHUNK_CLUSTER_RADIUS = 64+4;

	// DATA
	private ChunkClusterField clusterField = new ChunkClusterField(CHUNK_CLUSTER_RADIUS);

	// REFERENCES
	@Setter private ChunkClusterSource chunkClusterSource;

	// SETTINGS
	/**
	 * A ChunkCluster takes up at most 32MiB of memory.
	 * <p>
	 * So:
	 * 128 ChunkClusters ^= max 4GiB
	 * 256 ChunkClusters ^= max 8GiB
	 */
	@Getter @Setter private int maximumLoadedChunkClusters = 128;


	// INIT
	public World(ChunkClusterSource chunkClusterSource)
	{
		this.chunkClusterSource = chunkClusterSource;
	}


	// GETTERS
	public int getMaterialId(int x, int y, int z)
	{
		if(y < 0 || y >= Chunk.HEIGHT)
			return Material.AIR.id;

		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		return getUnsignedByteValue(chunk.getMaterialId(icX, y, icZ));
	}

	public int getMaterialSubId(int x, int y, int z)
	{
		if(y < 0 || y >= Chunk.HEIGHT)
			return Material.AIR.subId;

		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		return getUnsignedByteValue(chunk.getMaterialSubId(icX, y, icZ));
	}

	private int getUnsignedByteValue(byte input)
	{
		// TODO improve method signatures to make this unnecessary/hide it further down
		return input&0xFF;
	}


	// SETTERS
	public void setMaterialIdAndSubId(int x, int y, int z, byte materialId, byte materialSubId)
	{
		if(y < 0 || y >= Chunk.HEIGHT)
			return;

		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		chunk.setMaterialIdAndSubId(icX, y, icZ, materialId, materialSubId);
	}

	public void setMaterial(int x, int y, int z, Material material)
	{
		setMaterialIdAndSubId(x, y, z, (byte) material.id, (byte) material.subId);
	}


	// CHUNK AND CLUSTER
	public Chunk getChunkAt(int x, int z)
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
		ChunkCluster chunkCluster = this.clusterField.getCluster(clX, clZ);

		// chunk cluster not loaded, loading it
		if(chunkCluster == null)
			chunkCluster = loadChunkCluster(clX, clZ);

		return chunkCluster;
	}


	// LOADING/SAVING
	private ChunkCluster loadChunkCluster(int clX, int clZ)
	{
		// if number of currently loaded chunk clusters is too high, unload
		while(this.clusterField.getClusterList().size() >= this.maximumLoadedChunkClusters)
			unloadLongestUnusedChunkCluster();

		ChunkCluster chunkCluster = this.chunkClusterSource.loadChunkCluster(clX, clZ);

		// chunk cluster not saved to disk, create new chunk cluster
		if(chunkCluster == null)
			chunkCluster = new ChunkCluster(clX, clZ);

		this.clusterField.addCluster(chunkCluster);
		return chunkCluster;
	}

	private void unloadLongestUnusedChunkCluster()
	{
		ChunkCluster longestUnusedChunkCluster = this.clusterField.getLongestUnusedChunkCluster();

		this.chunkClusterSource.saveChunkCluster(longestUnusedChunkCluster);
		this.clusterField.removeCluster(longestUnusedChunkCluster);
	}

	public void save()
	{
		for(ChunkCluster cluster : this.clusterField.getClusterList())
			this.chunkClusterSource.saveChunkCluster(cluster);
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
			return ((elementXorZ%containerWidth)+containerWidth)%containerWidth;

		return elementXorZ%containerWidth;
	}

}
