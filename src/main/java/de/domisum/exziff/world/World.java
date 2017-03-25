package de.domisum.exziff.world;

public class World
{

	// CONSTANTS
	private static final int CHUNK_CLUSTER_RADIUS = 32;

	// DATA
	private ChunkClusterField clusterField = new ChunkClusterField(CHUNK_CLUSTER_RADIUS);


	// -------
	// INIT
	// -------
	public World()
	{

	}


	// -------
	// GETTERS
	// -------
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

	private Chunk getChunkAt(int x, int z)
	{
		int cX = getChunkXorZ(x);
		int cZ = getChunkXorZ(z);

		int clX = getChunkClusterXorZ(cX);
		int clZ = getChunkClusterXorZ(cZ);
		ChunkCluster chunkCluster = this.clusterField.getChunkCluster(clX, clZ);
		if(chunkCluster == null)
			return null;

		int iclX = getInChunkClusterXorZ(cX);
		int iclZ = getInChunkClusterXorZ(cZ);
		Chunk chunk = chunkCluster.getChunk(iclX, iclZ);
		return chunk;
	}


	// -------
	// SETTERS
	// -------
	public void setMaterialIdAndSubId(int x, int y, int z, byte materialId, byte materialSubId)
	{
		Chunk chunk = getChunkAt(x, z);
		int icX = getInChunkXOrZ(x);
		int icZ = getInChunkXOrZ(z);

		chunk.setMaterialIdAndSubId(icX, y, icZ, materialId, materialSubId);
	}


	// INTERNAL
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
