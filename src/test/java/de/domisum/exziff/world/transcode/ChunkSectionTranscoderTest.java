package de.domisum.exziff.world.transcode;

import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkSection;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class ChunkSectionTranscoderTest
{

	@Test public void testEncodeDecodePredefinedHomogenous()
	{
		ChunkSectionTranscoder transcoder = new ChunkSectionTranscoder();

		testEncodeDecodeEncodeSame(transcoder, new ChunkSection((byte) 0, (byte) 0));
		testEncodeDecodeEncodeSame(transcoder, new ChunkSection((byte) 1, (byte) 1));
		testEncodeDecodeEncodeSame(transcoder, new ChunkSection((byte) 255, (byte) 15));
	}

	@Test public void testEndcodeDecodePredefinedHeterogenous()
	{
		ChunkSectionTranscoder transcoder = new ChunkSectionTranscoder();

		byte[] blockData = new byte[ChunkSection.NUMBER_OF_BLOCKS*2];
		testEncodeDecodeEncodeSame(transcoder, new ChunkSection(blockData));

		blockData = new byte[ChunkSection.NUMBER_OF_BLOCKS*2];
		for(int i = 0; i < blockData.length; i++)
			blockData[i] = (byte) (i%17);
		testEncodeDecodeEncodeSame(transcoder, new ChunkSection(blockData));

		blockData = new byte[ChunkSection.NUMBER_OF_BLOCKS*2];
		for(int i = 0; i < blockData.length; i++)
			blockData[i] = (byte) (i*4359349%19237-34347789);
		testEncodeDecodeEncodeSame(transcoder, new ChunkSection(blockData));
	}

	@Test public void testEncodeDecodeRandomized()
	{
		ChunkSectionTranscoder transcoder = new ChunkSectionTranscoder();
		Random random = new Random(0x7e57);

		for(int test = 0; test < 1000; test++)
		{
			byte[] blockData = new byte[ChunkSection.NUMBER_OF_BLOCKS*2];
			random.nextBytes(blockData);

			testEncodeDecodeEncodeSame(transcoder, new ChunkSection(blockData));
		}
	}


	private void testEncodeDecodeEncodeSame(ChunkSectionTranscoder transcoder, ChunkSection chunkSection)
	{
		byte[] encoded = transcoder.encode(chunkSection);
		ChunkSection decoded = transcoder.decode(encoded);
		assertEqualsChunkSection(chunkSection, decoded);

		byte[] encodedAgain = transcoder.encode(decoded);
		Assert.assertArrayEquals(encoded, encodedAgain);
	}

	public static void assertEqualsChunkSection(ChunkSection chunkSection1, ChunkSection chunkSection2)
	{
		for(int icsY = 0; icsY < ChunkSection.HEIGHT; icsY++)
			for(int icsZ = 0; icsZ < Chunk.WIDTH; icsZ++)
				for(int icsX = 0; icsX < Chunk.WIDTH; icsX++)
				{
					Assert.assertEquals("Difference in chunk section materialId at x"+icsX+" y"+icsY+" z"+icsZ,
							chunkSection1.getMaterialId(icsX, icsY, icsZ), chunkSection2.getMaterialId(icsX, icsY, icsZ));

					Assert.assertEquals("Difference in chunk section materialSubId at x"+icsX+" y"+icsY+" z"+icsZ,
							chunkSection1.getMaterialSubId(icsX, icsY, icsZ), chunkSection2.getMaterialSubId(icsX, icsY, icsZ));
				}
	}

}
