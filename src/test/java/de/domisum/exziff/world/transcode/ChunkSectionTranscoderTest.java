package de.domisum.exziff.world.transcode;

import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkSection;
import de.domisum.exziff.world.block.Axis;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.HorizontalOrientation;
import de.domisum.exziff.world.block.Material;
import de.domisum.exziff.world.block.VerticalOrientation;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChunkSectionTranscoderTest
{

	private static final Collection<Material> MATERIALS_WITHOUT_ATTRIBUTES;
	private static final Collection<Block> SOME_BLOCKS;

	static
	{
		List<Material> materialsWithoutAttributes = new ArrayList<>();
		for(Material material : Material.values())
			if(material.blockAttributes.isEmpty())
				materialsWithoutAttributes.add(material);

		MATERIALS_WITHOUT_ATTRIBUTES = Collections.unmodifiableList(materialsWithoutAttributes);


		List<Block> someBlocks = new ArrayList<>();
		someBlocks.add(new BlockBuilder(Material.LOG_SPRUCE).set(Axis.X).build());
		someBlocks.add(new BlockBuilder(Material.LOG_SPRUCE).set(Axis.Y).build());
		someBlocks.add(new BlockBuilder(Material.LOG_SPRUCE).set(Axis.Z).build());
		someBlocks.add(new BlockBuilder(Material.LOG_SPRUCE).set(Axis.NONE).build());
		someBlocks.add(new BlockBuilder(Material.STAIR_OAK)
				.set(HorizontalOrientation.BOTTOM)
				.set(VerticalOrientation.TOWARD_POSITIVE_X)
				.build());
		someBlocks.add(new BlockBuilder(Material.LEAVES_OAK).build());
		someBlocks.add(new BlockBuilder(Material.STONE).build());

		SOME_BLOCKS = Collections.unmodifiableCollection(someBlocks);
	}


	// TESTS
	@Test
	public void testEncodeDecodeSetHomogenous()
	{
		ChunkSectionTranscoder transcoder = new ChunkSectionTranscoder();

		assertEncodeDecodeEquals(transcoder, new ChunkSection(Material.AIR));
		assertEncodeDecodeEquals(transcoder, new ChunkSection(Material.LEAVES_SPRUCE));
	}

	@Test
	public void testEncodeDecodeRandomizedHeterogenousNoAttributes()
	{
		ChunkSectionTranscoder transcoder = new ChunkSectionTranscoder();
		Random random = new Random(0xaffe);

		for(int test = 0; test < 100; test++)
			assertEncodeDecodeEquals(transcoder, generateRandomChunkSectionNoAttributes(random));
	}

	@Test
	public void testEncodeDecodeRandomizedHeterogenousWithAttributes()
	{
		ChunkSectionTranscoder transcoder = new ChunkSectionTranscoder();
		Random random = new Random(0xaffe);

		for(int test = 0; test < 10; test++)
			assertEncodeDecodeEquals(transcoder, generateRandomChunkSectionWithAttributes(random));
	}

	private void assertEncodeDecodeEquals(ChunkSectionTranscoder transcoder, ChunkSection chunkSection)
	{
		byte[] encoded = transcoder.encode(chunkSection);
		ChunkSection decoded = transcoder.decode(encoded);
		assertEqualsChunkSection(chunkSection, decoded);
	}


	// TEST VALIDATORS
	public static void assertEqualsChunkSection(ChunkSection chunkSection1, ChunkSection chunkSection2)
	{
		for(int icsY = 0; icsY < ChunkSection.HEIGHT; icsY++)
			for(int icsZ = 0; icsZ < Chunk.WIDTH; icsZ++)
				for(int icsX = 0; icsX < Chunk.WIDTH; icsX++)
					Assertions.assertEquals(
							chunkSection1.getBlock(icsX, icsY, icsZ),
							chunkSection2.getBlock(icsX, icsY, icsZ),
							"Difference in chunk section block at x"+icsX+" y"+icsY+" z"+icsZ
					);
	}


	// TESTCASE GENERATOR
	public static ChunkSection generateRandomChunkSectionNoAttributes(Random random)
	{
		ChunkSection chunkSection = new ChunkSection();

		for(int x = 0; x < Chunk.WIDTH; x++)
			for(int y = 0; y < ChunkSection.HEIGHT; y++)
				for(int z = 0; z < Chunk.WIDTH; z++)
				{
					Material randomMaterial = RandomUtil.getElement(MATERIALS_WITHOUT_ATTRIBUTES, random);
					Block randomBlock = new BlockBuilder(randomMaterial).build();
					chunkSection.setBlock(x, y, z, randomBlock);
				}

		return chunkSection;
	}

	public static ChunkSection generateRandomChunkSectionWithAttributes(Random random)
	{
		ChunkSection chunkSection = new ChunkSection();

		for(int x = 0; x < Chunk.WIDTH; x++)
			for(int y = 0; y < ChunkSection.HEIGHT; y++)
				for(int z = 0; z < Chunk.WIDTH; z++)
				{
					Block randomBlock = RandomUtil.getElement(SOME_BLOCKS, random);
					chunkSection.setBlock(x, y, z, randomBlock);
				}

		return chunkSection;
	}

}
