package de.domisum.exziff.world.transcode;

import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkSection;
import de.domisum.exziff.world.block.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class ChunkTranscoderTest
{

	@Test
	public void testEncodeDecodePredefinedHomogenous()
	{
		ChunkTranscoder transcoder = new ChunkTranscoder();

		ChunkSection chunkSectionHomogenous1 = new ChunkSection(Material.AIR);
		ChunkSection chunkSectionHomogenous2 = new ChunkSection(Material.LEAVES_SPRUCE);

		ChunkSection[] chunkSections = new ChunkSection[Chunk.NUMBER_OF_SECTIONS];
		for(int i = 0; i < chunkSections.length; i++)
			chunkSections[i] = chunkSectionHomogenous1;
		assertEncodeDecodeEquals(transcoder, new Chunk(3, 8, chunkSections));

		ChunkSection[] sections = new ChunkSection[Chunk.NUMBER_OF_SECTIONS];
		for(int i = 0; i < sections.length; i++)
			sections[i] = ((i%2) == 0) ? chunkSectionHomogenous1 : chunkSectionHomogenous2;
		assertEncodeDecodeEquals(transcoder, new Chunk(-7, 0, sections));

		assertEncodeDecodeEquals(transcoder, new Chunk(7, 7));
	}

	@Test
	public void testEncodeDecodeRandomized()
	{
		ChunkTranscoder transcoder = new ChunkTranscoder();
		Random random = new Random(0xdab);

		for(int test = 0; test < 30; test++)
			assertEncodeDecodeEquals(transcoder, generateRandomChunk(random));
	}


	private void assertEncodeDecodeEquals(ChunkTranscoder transcoder, Chunk chunk)
	{
		byte[] encoded = transcoder.encode(chunk);
		Chunk decoded = transcoder.decode(encoded);
		assertEqualsChunk(chunk, decoded);
	}


	// VALIDATORS
	public static void assertEqualsChunk(Chunk chunk1, Chunk chunk2)
	{
		Assertions.assertEquals(chunk1.getCX(), chunk2.getCX(), "cX not equal");
		Assertions.assertEquals(chunk1.getCZ(), chunk2.getCZ(), "cZ not equal");

		for(int section = 0; section < Chunk.NUMBER_OF_SECTIONS; section++)
		{
			ChunkSection section1 = chunk1.getChunkSections()[section];
			ChunkSection section2 = chunk2.getChunkSections()[section];

			ChunkSectionTranscoderTest.assertEqualsChunkSection(section1, section2);
		}
	}

	// TEST CASE GENERATOR
	public static Chunk generateRandomChunk(Random random)
	{
		ChunkSection[] chunkSections = new ChunkSection[Chunk.NUMBER_OF_SECTIONS];
		for(int i = 0; i < Chunk.NUMBER_OF_SECTIONS; i++)
			chunkSections[i] = ChunkSectionTranscoderTest.generateRandomChunkSectionNoAttributes(random);

		return new Chunk(random.nextInt(), random.nextInt(), chunkSections);
	}

}
