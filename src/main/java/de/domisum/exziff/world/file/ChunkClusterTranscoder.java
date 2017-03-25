package de.domisum.exziff.world.file;

import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkCluster;

public class ChunkClusterTranscoder implements Transcoder<ChunkCluster>
{

	/*
	Format:

	The first 8 bytes contain two integers, representing the Chunk's cX and cY coordinate.

	The following Chunk.NUMBER_OF_SECTIONS*4 bytes are occupied with Chunk.NUMBER_OF_SECTIONS integers,
	which hold the length of their respective ChunkSection in the byte array.

	The rest of the bytes are occupied with the Chunks in sequence.
	 */


	// CONSTANTS
	private static final int BYTES_BEFORE_CHUNK_DATA = (2+ChunkCluster.NUMBER_OF_CHUNKS)*4;

	// REFERENCES
	private ChunkTranscoder chunkTranscoder = new ChunkTranscoder();


	// TRANSCODING
	@Override public byte[] encode(ChunkCluster toEncode)
	{
		byte[][] encodedChunks = new byte[ChunkCluster.NUMBER_OF_CHUNKS][];
		int encodedChunksCombinedLength = 0;

		for(int i = 0; i < ChunkCluster.NUMBER_OF_CHUNKS; i++)
		{
			Chunk chunk = toEncode.getChunks()[i];

			byte[] encodedChunk = this.chunkTranscoder.encode(chunk);
			encodedChunks[i] = encodedChunk;
			encodedChunksCombinedLength += encodedChunk.length;
		}

		byte[] encodedChunkCluster = new byte[BYTES_BEFORE_CHUNK_DATA+encodedChunksCombinedLength];
		encodeInt(encodedChunkCluster, 0, toEncode.getClX());
		encodeInt(encodedChunkCluster, 4, toEncode.getClZ());

		int currentWritingPosition = BYTES_BEFORE_CHUNK_DATA;
		for(int i = 0; i < ChunkCluster.NUMBER_OF_CHUNKS; i++)
		{
			byte[] encodedChunk = encodedChunks[i];

			// write chunk length
			encodeInt(encodedChunkCluster, (2+i)*4, encodedChunk.length);

			// write chunk data itself
			System.arraycopy(encodedChunk, 0, encodedChunkCluster, currentWritingPosition, encodedChunk.length);

			currentWritingPosition += encodedChunk.length;
		}

		return encodedChunkCluster;
	}

	@Override public ChunkCluster decode(byte[] toDecode)
	{
		int clX = decodeInt(toDecode, 0);
		int clZ = decodeInt(toDecode, 4);
		Chunk[] chunks = new Chunk[ChunkCluster.NUMBER_OF_CHUNKS];

		int currentReadingPosition = BYTES_BEFORE_CHUNK_DATA;
		for(int i = 0; i < Chunk.NUMBER_OF_SECTIONS; i++)
		{
			int chunkLength = decodeInt(toDecode, (2+i)*4);

			// isolate byte array
			byte[] chunkData = new byte[chunkLength];
			System.arraycopy(toDecode, currentReadingPosition, chunkData, 0, chunkLength);

			// decode chunk
			Chunk chunk = this.chunkTranscoder.decode(chunkData);
			chunks[i] = chunk;

			currentReadingPosition += chunkLength;
		}

		return new ChunkCluster(clX, clZ, chunks);
	}

}
