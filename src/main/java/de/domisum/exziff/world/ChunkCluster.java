package de.domisum.exziff.world;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

public class ChunkCluster
{

	// CONSTANTS
	public static final int WIDTH = 16;

	// DATA
	@Getter private final int clX;
	@Getter private final int clZ;
	private Chunk[] chunks = new Chunk[WIDTH*WIDTH];


	// -------
	// INIT
	// -------
	public ChunkCluster(int clX, int clZ, Chunk[] chunks)
	{
		this.clX = clX;
		this.clZ = clZ;

		Validate.isTrue(chunks.length == WIDTH*WIDTH, "The number of chunks has to be "+(WIDTH*WIDTH));
		Validate.noNullElements(chunks);
		this.chunks = chunks;
	}


	// -------
	// GETTERS
	// -------
	public Chunk getChunk(int iclX, int iclZ)
	{
		int chunkInClusterIndex = getChunkInClusterIndex(iclX, iclZ);

		Chunk chunk = this.chunks[chunkInClusterIndex];
		return chunk;
	}


	// INTERNAL
	private int getChunkInClusterIndex(int iclX, int iclZ)
	{
		return (iclX)+(iclZ*WIDTH);
	}

}
