package de.domisum.exziff.generator.tree.generators.needle.spruce;

import de.domisum.exziff.blockstructure.BlockStructure;
import de.domisum.exziff.generator.tree.TreeGenerator;
import de.domisum.exziff.generator.tree.TreeGeneratorContext;
import de.domisum.exziff.world.block.Axis;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.util.Random;

public class TreeGeneratorSpruceTiny implements TreeGenerator
{

	@Override
	public BlockStructure generate(long seed, TreeGeneratorContext treeGeneratorContext)
	{
		return new GenerateMethodObject(treeGeneratorContext, seed).generate();
	}

	private static class GenerateMethodObject
	{

		// INPUT
		private final TreeGeneratorContext treeGeneratorContext;

		// TEMP
		private final Random random;
		private final BlockStructure blockStructure = new BlockStructure();

		private int height = -1;


		// INIT
		public GenerateMethodObject(TreeGeneratorContext treeGeneratorContext, long seed)
		{
			this.treeGeneratorContext = treeGeneratorContext;
			random = new Random(seed);
		}


		// GENERATE
		public BlockStructure generate()
		{
			height = RandomUtil.getFromRange(6, 8, random);
			for(int y = 0; y < height; y++)
				generateLevel(y);

			return blockStructure;
		}

		private void generateLevel(int y)
		{
			generateTrunk(y);
			generateLeaves(y);
		}

		private void generateTrunk(int y)
		{
			Block block;

			if((y/(double) height) <= 0.6)
				block = new BlockBuilder(Material.LOG_SPRUCE).set(Axis.class, Axis.Y).build();
			else
				block = new BlockBuilder(Material.LEAVES_SPRUCE).build();

			blockStructure.setBlock(0, y, 0, block);
		}

		private void generateLeaves(int y)
		{

		}

	}
}
