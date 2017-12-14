package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.world.Material;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BlockStackMerger
{

	// INPUT
	private final List<WeightedBlockStack> weightedBlockStacks;


	// MERGE
	public BlockStack merge()
	{
		if(weightedBlockStacks.size() == 1)
			return weightedBlockStacks.get(0).blockStack;

		// TODO actually implement some complex merging

		double weightedMaxYSum = 0;
		double weightSum = 0;
		for(WeightedBlockStack wbs : weightedBlockStacks)
		{
			weightedMaxYSum += wbs.blockStack.getMaximumY()*wbs.weight;
			weightSum += wbs.weight;
		}

		int newMaxY = (int) Math.round(weightedMaxYSum/weightSum);
		return BlockStack.fromMaterialAndMaxY(Material.STONE, newMaxY);
	}


	// VALUED BLOCK STACK
	@RequiredArgsConstructor
	public static class WeightedBlockStack
	{

		private final BlockStack blockStack;
		private final double weight;

	}

}
