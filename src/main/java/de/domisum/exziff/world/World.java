package de.domisum.exziff.world;

import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.chunkclusterstorage.ChunkClusterStorage;
import lombok.Getter;
import lombok.Setter;

public class World
{

	// CONSTANTS
	private static final int CHUNK_CLUSTER_RADIUS = 256+16;

	// DATA
	private final ChunkClusterField clusterField = new ChunkClusterField(CHUNK_CLUSTER_RADIUS);

	// REFERENCES
	@Setter
	private ChunkClusterStorage chunkClusterStorage;

	// SETTINGS
	/**
	 * A ChunkCluster takes up at least 32MiB of memory.
	 * <p>
	 * Assuming no BlockAttributes:
	 * 128 ChunkClusters ^= 4 GiB
	 * 256 ChunkClusters ^= 8 GiB
	 * 512 ChunkClusters ^= 16 GiB
	 * 1024 ChunkClusters ^= 32 GiB
	 */
	@Getter
	@Setter
	private int maximumLoadedChunkClusters = 128;


	// INIT
	public World(ChunkClusterStorage chunkClusterStorage)
	{
		this.chunkClusterStorage = chunkClusterStorage;
	}


	// GETTERS
	public Block getBlock(int x, int y, int z)
	{
		if((y < 0) || (y >= Chunk.HEIGHT))
			throw new IllegalArgumentException("supplied y value is out of bounds: "+y);

		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		return chunk.getBlock(icX, y, icZ);
	}


	// SETTERS
	public void setBlock(int x, int y, int z, Block block)
	{
		if((y < 0) || (y >= Chunk.HEIGHT))
			throw new IllegalArgumentException("supplied y value is out of bounds: "+y);

		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		chunk.setBlock(icX, y, icZ, block);
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
		ChunkCluster chunkCluster = clusterField.getCluster(clX, clZ);

		// chunk cluster not loaded, loading it
		if(chunkCluster == null)
			chunkCluster = loadChunkCluster(clX, clZ);

		return chunkCluster;
	}


	// LOADING/SAVING
	private ChunkCluster loadChunkCluster(int clX, int clZ)
	{
		// if number of currently loaded chunk clusters is too high, unload
		while(clusterField.getClusterList().size() >= maximumLoadedChunkClusters)
			unloadLongestUnusedChunkCluster();

		ChunkCluster chunkCluster = chunkClusterStorage.loadChunkCluster(clX, clZ);

		// chunk cluster was not saved on disk, create new chunk cluster
		if(chunkCluster == null)
			chunkCluster = new ChunkCluster(clX, clZ);

		clusterField.addCluster(chunkCluster);
		return chunkCluster;
	}

	private void unloadLongestUnusedChunkCluster()
	{
		ChunkCluster longestUnusedChunkCluster = clusterField.getLongestUnusedChunkCluster();

		chunkClusterStorage.saveChunkCluster(longestUnusedChunkCluster);
		clusterField.removeCluster(longestUnusedChunkCluster);
	}

	public void save()
	{
		for(ChunkCluster cluster : clusterField.getClusterList())
			chunkClusterStorage.saveChunkCluster(cluster);
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
			return ((elementXorZ+1)/containerWidth)-1;

		return elementXorZ/containerWidth;
	}

	private static int getInContainerXorZ(int elementXorZ, int containerWidth)
	{
		if(elementXorZ < 0)
			return ((elementXorZ%containerWidth)+containerWidth)%containerWidth;

		return elementXorZ%containerWidth;
	}

}
