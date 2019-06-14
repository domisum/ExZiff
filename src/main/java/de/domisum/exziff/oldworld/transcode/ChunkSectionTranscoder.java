package de.domisum.exziff.oldworld.transcode;

import de.domisum.exziff.oldworld.ChunkSection;

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


	// ENCODE
	@Override
	public byte[] encode(ChunkSection chunkSection)
	{
		chunkSection.optimize();

		if(chunkSection.isHomogenous())
			return encodeHomogenous(chunkSection);
		else
			return encodeHeterogenous(chunkSection);
	}

	private byte[] encodeHomogenous(ChunkSection chunkSection)
	{
		byte homogenousMaterialId = chunkSection.getMaterialId(0, 0, 0);
		byte homogenousMaterialSubId = chunkSection.getMaterialSubId(0, 0, 0);

		return new byte[] {TYPE_HOMOGENOUS, homogenousMaterialId, homogenousMaterialSubId};
	}

	private byte[] encodeHeterogenous(ChunkSection chunkSection)
	{
		byte[] blockData = chunkSection.getBlockData();

		byte[] chunkSectionData = new byte[1+blockData.length];
		chunkSectionData[0] = TYPE_HETEROGENOUS;
		System.arraycopy(blockData, 0, chunkSectionData, 1, blockData.length);

		return chunkSectionData;
	}


	// DECODE
	@Override
	public ChunkSection decode(byte[] toDecode)
	{
		byte type = toDecode[0];
		if(type == TYPE_HOMOGENOUS)
			return decodeHomogenous(toDecode);
		else
			return decodeHeterogenous(toDecode);
	}

	private ChunkSection decodeHomogenous(byte[] toDecode)
	{
		byte homogenousMaterialId = toDecode[1];
		byte homogenousMaterialSubId = toDecode[2];

		return new ChunkSection(homogenousMaterialId, homogenousMaterialSubId);
	}

	private ChunkSection decodeHeterogenous(byte[] toDecode)
	{
		byte[] blockData = new byte[toDecode.length-1];
		int blockDataStart = 1;
		System.arraycopy(toDecode, blockDataStart, blockData, 0, blockData.length);

		return new ChunkSection(blockData);
	}

}
