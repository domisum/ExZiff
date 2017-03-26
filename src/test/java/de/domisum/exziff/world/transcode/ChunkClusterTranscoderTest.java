package de.domisum.exziff.world.transcode;

import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkCluster;
import de.domisum.exziff.world.ChunkSection;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class ChunkClusterTranscoderTest
{

	@Test public void testEncodeDecodePredefined()
	{
		ChunkClusterTranscoder transcoder = new ChunkClusterTranscoder();

		ChunkSection chunkSectionHomogenous1 = new ChunkSection((byte) 0, (byte) 0);

		ChunkSection[] chunkSections = new ChunkSection[Chunk.NUMBER_OF_SECTIONS];
		for(int i = 0; i < chunkSections.length; i++)
			chunkSections[i] = chunkSectionHomogenous1;
		Chunk chunk = new Chunk(3, 8, chunkSections);

		Chunk[] chunks = new Chunk[ChunkCluster.NUMBER_OF_CHUNKS];
		for(int i = 0; i < chunks.length; i++)
			chunks[i] = chunk;

		testEncodeDecodeEquals(transcoder, new ChunkCluster(0, 5, chunks));
	}

	@Test public void testEncodeDecodeRandomized()
	{
		ChunkClusterTranscoder transcoder = new ChunkClusterTranscoder();
		Random random = new Random(0x8877);

		for(int test = 0; test < 50; test++)
			testEncodeDecodeEquals(transcoder, generateChunkCluster(random));
	}


	private void testEncodeDecodeEquals(ChunkClusterTranscoder transcoder, ChunkCluster chunkCluster)
	{
		byte[] encoded = transcoder.encode(chunkCluster);
		ChunkCluster decoded = transcoder.decode(encoded);
		assertEqualsChunkCluster(chunkCluster, decoded);

		byte[] encodedAgain = transcoder.encode(decoded);
		Assert.assertArrayEquals("encodedAgain not equal to first encoded", encoded, encodedAgain);
	}


	// VALIDATORS
	private static void assertEqualsChunkCluster(ChunkCluster chunkCluster1, ChunkCluster chunkCluster2)
	{
		Assert.assertEquals("clX not equal", chunkCluster1.getClX(), chunkCluster2.getClX());
		Assert.assertEquals("clZ not equal", chunkCluster1.getClZ(), chunkCluster2.getClZ());

		for(int chunkIndex = 0; chunkIndex < Chunk.NUMBER_OF_SECTIONS; chunkIndex++)
		{
			Chunk chunk1 = chunkCluster1.getChunks()[chunkIndex];
			Chunk chunk2 = chunkCluster2.getChunks()[chunkIndex];

			ChunkTranscoderTest.assertEqualsChunk(chunk1, chunk2);
		}
	}


	// TEST CASE GENERATOR
	private static ChunkCluster generateChunkCluster(Random random)
	{
		Chunk[] chunks = new Chunk[ChunkCluster.NUMBER_OF_CHUNKS];
		for(int i = 0; i < chunks.length; i++)
			chunks[i] = ChunkTranscoderTest.generateRandomChunk(random);

		return new ChunkCluster(random.nextInt(), random.nextInt(), chunks);
	}

}
