package de.domisum.exziff.world.transcode;

import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkCluster;
import de.domisum.exziff.world.ChunkSection;
import de.domisum.exziff.world.block.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

public class ChunkClusterTranscoderTest
{

	@Test
	public void testEncodeDecodePredefined()
	{
		ChunkClusterTranscoder transcoder = new ChunkClusterTranscoder();

		ChunkSection chunkSectionHomogenous1 = new ChunkSection(Material.AIR);

		ChunkSection[] chunkSections = new ChunkSection[Chunk.NUMBER_OF_SECTIONS];
		Arrays.fill(chunkSections, chunkSectionHomogenous1);
		Chunk chunk = new Chunk(3, 8, chunkSections);

		Chunk[] chunks = new Chunk[ChunkCluster.NUMBER_OF_CHUNKS];
		Arrays.fill(chunks, chunk);

		assertEncodeDecodeEquals(transcoder, new ChunkCluster(0, 5, chunks));
	}

	@Test
	public void testEncodeDecodeRandomized()
	{
		ChunkClusterTranscoder transcoder = new ChunkClusterTranscoder();
		Random random = new Random(0x88776);

		assertEncodeDecodeEquals(transcoder, generateChunkCluster(random));
	}


	private void assertEncodeDecodeEquals(ChunkClusterTranscoder transcoder, ChunkCluster chunkCluster)
	{
		byte[] encoded = transcoder.encode(chunkCluster);
		ChunkCluster decoded = transcoder.decode(encoded);
		assertEqualsChunkCluster(chunkCluster, decoded);

		byte[] encodedAgain = transcoder.encode(decoded);
		Assertions.assertArrayEquals(encoded, encodedAgain, "encodedAgain not equal to first encoded");
	}


	// VALIDATORS
	private static void assertEqualsChunkCluster(ChunkCluster chunkCluster1, ChunkCluster chunkCluster2)
	{
		Assertions.assertEquals(chunkCluster1.getClX(), chunkCluster2.getClX(), "clX not equal");
		Assertions.assertEquals(chunkCluster1.getClZ(), chunkCluster2.getClZ(), "clZ not equal");

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
		Arrays.setAll(chunks, i->ChunkTranscoderTest.generateRandomChunk(random));

		return new ChunkCluster(random.nextInt(), random.nextInt(), chunks);
	}

}
