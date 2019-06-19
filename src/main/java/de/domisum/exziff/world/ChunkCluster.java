package de.domisum.exziff.world;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

public class ChunkCluster
{

	// CONSTANTS
	public static final int WIDTH = 16;
	public static final int NUMBER_OF_CHUNKS = WIDTH*WIDTH;

	// DATA
	@Getter private final int clX;
	@Getter private final int clZ;
	@Getter private Chunk[] chunks;


	// INIT
	public ChunkCluster(int clX, int clZ, Chunk[] chunks)
	{
		this.clX = clX;
		this.clZ = clZ;

		Validate.isTrue(chunks.length == NUMBER_OF_CHUNKS, "The number of chunks has to be "+NUMBER_OF_CHUNKS);
		Validate.noNullElements(chunks);
		this.chunks = chunks;
	}

	public ChunkCluster(int clX, int clZ)
	{
		this.clX = clX;
		this.clZ = clZ;

		this.chunks = new Chunk[NUMBER_OF_CHUNKS];
		for(int iclZ = 0; iclZ < WIDTH; iclZ++)
			for(int iclX = 0; iclX < WIDTH; iclX++)
			{
				Chunk chunk = new Chunk(clX*WIDTH+iclX, clZ*WIDTH+iclZ);
				this.chunks[getChunkInClusterIndex(iclX, iclZ)] = chunk;
			}
	}


	// OBJECT
	@Override public String toString()
	{
		return "ChunkCluster{"+"clX="+this.clX+", clZ="+this.clZ+'}';
	}


	// GETTERS
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
