package de.domisum.exziff.world.transcode;

import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkSection;

public class ChunkTranscoder implements Transcoder<Chunk>
{

	/*
	Format:

	The first 8 bytes contain two integers, representing the Chunk's cX and cY coordinate.

	The following Chunk.NUMBER_OF_SECTIONS*4 bytes are occupied with Chunk.NUMBER_OF_SECTIONS integers,
	which hold the length of their respective ChunkSection in the byte array.

	The rest of the bytes are occupied with the ChunkSections in sequence.
	 */

	// CONSTANTS
	private static final int BYTES_BEFORE_SECTION_DATA = (2+Chunk.NUMBER_OF_SECTIONS)*4;

	// REFERENCES
	private ChunkSectionTranscoder chunkSectionTranscoder = new ChunkSectionTranscoder();


	// TRANSCODING
	@Override public byte[] encode(Chunk toEncode)
	{
		byte[][] encodedChunkSections = new byte[Chunk.NUMBER_OF_SECTIONS][];
		int encodedChunkSectionsCombinedLength = 0;

		for(int i = 0; i < Chunk.NUMBER_OF_SECTIONS; i++)
		{
			byte[] encodedChunkSection = this.chunkSectionTranscoder.encode(toEncode.getChunkSections()[i]);

			encodedChunkSections[i] = encodedChunkSection;
			encodedChunkSectionsCombinedLength += encodedChunkSection.length;
		}

		byte[] encodedChunk = new byte[BYTES_BEFORE_SECTION_DATA+encodedChunkSectionsCombinedLength];
		encodeInt(encodedChunk, 0, toEncode.getCX());
		encodeInt(encodedChunk, 4, toEncode.getCZ());

		int currentWritingPosition = BYTES_BEFORE_SECTION_DATA;
		for(int i = 0; i < Chunk.NUMBER_OF_SECTIONS; i++)
		{
			byte[] encodedChunkSection = encodedChunkSections[i];

			// write the length of the ChunkSection
			encodeInt(encodedChunk, (2+i)*4, encodedChunkSection.length);

			// write chunk section
			System.arraycopy(encodedChunkSection, 0, encodedChunk, currentWritingPosition, encodedChunkSection.length);

			currentWritingPosition += encodedChunkSection.length;
		}

		return encodedChunk;
	}

	@Override public Chunk decode(byte[] toDecode)
	{
		int cX = decodeInt(toDecode, 0);
		int cZ = decodeInt(toDecode, 4);
		ChunkSection[] chunkSections = new ChunkSection[Chunk.NUMBER_OF_SECTIONS];

		int currentReadingPosition = BYTES_BEFORE_SECTION_DATA;
		for(int i = 0; i < Chunk.NUMBER_OF_SECTIONS; i++)
		{
			int chunkSectionLength = decodeInt(toDecode, (2+i)*4);

			// isolate byte array
			byte[] chunkSectionData = new byte[chunkSectionLength];
			System.arraycopy(toDecode, currentReadingPosition, chunkSectionData, 0, chunkSectionLength);

			// decode chunk section
			ChunkSection chunkSection = this.chunkSectionTranscoder.decode(chunkSectionData);
			chunkSections[i] = chunkSection;

			currentReadingPosition += chunkSectionLength;
		}

		return new Chunk(cX, cZ, chunkSections);
	}

}
