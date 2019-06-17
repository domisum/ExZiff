package de.domisum.exziff.blocksource.sources;

import de.domisum.exziff.blocksource.BlockSource;
import de.domisum.exziff.world.block.Block;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class BlockSourceRandomized implements BlockSource
{

	private final Random random;
	private final Map<Block, Double> blocksWithChance;


	// INIT
	private BlockSourceRandomized(long seed, Map<Block, Double> blocksWithChance)
	{
		random = new Random(seed);
		this.blocksWithChance = blocksWithChance;
	}

	public static class BlockSourceRandomizedBuilder
	{

		private final long seed;
		private final Map<Block, Double> blocksWithChance = new HashMap<>();


		// INIT
		public BlockSourceRandomizedBuilder(long seed)
		{
			this.seed = seed;
		}

		public BlockSourceRandomizedBuilder putBlock(Block block, double chance)
		{
			blocksWithChance.put(block, chance);
			return this;
		}

		public BlockSourceRandomized build()
		{
			return new BlockSourceRandomized(seed, blocksWithChance);
		}

	}


	// GETTERS
	@Override
	public Block get(int x, int y, int z)
	{
		return RandomUtil.getElement(blocksWithChance, random);
	}

}
