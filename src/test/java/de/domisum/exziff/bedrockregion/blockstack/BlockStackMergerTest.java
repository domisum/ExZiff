package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.oldworld.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class BlockStackMergerTest
{

	@Test public void bothAreSameMerge()
	{
		assertBlockStackEquals(new BlockStackUniform(Material.STONE, 10),
				merge(new BlockStackMerger.WeightedBlockStack(new BlockStackUniform(Material.STONE, 10), 1)));

		assertBlockStackEquals(new BlockStackUniform(Material.STONE, 10),
				merge(new BlockStackMerger.WeightedBlockStack(new BlockStackUniform(Material.STONE, 10), 1),
						new BlockStackMerger.WeightedBlockStack(new BlockStackUniform(Material.AIR, 10), 0.1)));
	}


	private static BlockStack merge(BlockStackMerger.WeightedBlockStack... weightedBlockStacks)
	{
		return merge(Arrays.asList(weightedBlockStacks));
	}

	private static BlockStack merge(List<BlockStackMerger.WeightedBlockStack> weightedBlockStacks)
	{
		BlockStackMerger blockStackMerger = new BlockStackMerger(weightedBlockStacks);
		return blockStackMerger.merge();
	}


	private static void assertBlockStackEquals(BlockStack bs1, BlockStack bs2)
	{
		Assertions.assertEquals(bs1.getMaximumY(), bs2.getMaximumY(), "different maximumY");

		for(int y = 0; y <= bs1.getMaximumY(); y++)
			Assertions.assertEquals(bs1.getMaterialAt(y), bs2.getMaterialAt(y), "different material at y"+y);

	}

}
