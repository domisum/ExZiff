package de.domisum.exziff.world;

import de.domisum.exziff.world.block.Axis;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChunkSectionTest
{

	@Test
	public void testEncodeDecodeSetHomogenous()
	{
		ChunkSection chunkSection = new ChunkSection();

		Block spruceLog = new BlockBuilder(Material.LOG_SPRUCE).set(Axis.X).build();
		Block acaciaLeaves = new BlockBuilder(Material.LEAVES_ACACIA).build();

		chunkSection.setBlock(0, 0, 0, spruceLog);
		chunkSection.setBlock(0, 0, 0, acaciaLeaves);

		Assertions.assertEquals(chunkSection.getBlock(0, 0, 0), acaciaLeaves, "block not properly overwritten");
	}

}
