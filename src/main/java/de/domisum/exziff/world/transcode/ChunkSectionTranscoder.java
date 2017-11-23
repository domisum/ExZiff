package de.domisum.exziff.world.transcode;

import de.domisum.exziff.world.ChunkSection;

public class ChunkSectionTranscoder implements Transcoder<ChunkSection>
{

	/*
	Format:

	Encoding type is determined by byte 0.

	If the encoding type is TYPE_HOMOGENOUS:
	- byte 1: materialId of the whole ChunkSection
	- byte 2: materialSubId of the whole ChunkSection

	If the encoding type is TYPE_HETEROGENOUS:
	- byte 1 to last byte: the blockData of the ChunkSelection
	 */

	// CONSTANTS
	private static final byte TYPE_HOMOGENOUS = 0;
	private static final byte TYPE_HETEROGENOUS = 1;


	// TRANSCODING
	@Override public byte[] encode(ChunkSection chunkSection)
	{
		chunkSection.optimize();

		// homogenous
		if(chunkSection.isHomogenous())
			return new byte[] {TYPE_HOMOGENOUS, chunkSection.getMaterialId(0, 0, 0), chunkSection.getMaterialSubId(0, 0, 0)};

		// heterogenous
		byte[] blockData = chunkSection.getBlockData();

		byte[] chunkSectionData = new byte[1+blockData.length];
		chunkSectionData[0] = TYPE_HETEROGENOUS;
		System.arraycopy(blockData, 0, chunkSectionData, 1, blockData.length);

		return chunkSectionData;
	}

	@Override public ChunkSection decode(byte[] toDecode)
	{
		// homogenous
		if(toDecode[0] == TYPE_HOMOGENOUS)
			return new ChunkSection(toDecode[1], toDecode[2]);

		// heterogenous
		byte[] blockData = new byte[toDecode.length-1];
		System.arraycopy(toDecode, 1, blockData, 0, blockData.length);
		ChunkSection chunkSection = new ChunkSection(blockData);

		return chunkSection;
	}

}
