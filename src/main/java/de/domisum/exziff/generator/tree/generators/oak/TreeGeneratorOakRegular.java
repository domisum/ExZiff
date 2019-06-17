package de.domisum.exziff.generator.tree.generators.oak;

import de.domisum.exziff.blockstructure.BlockStructure;
import de.domisum.exziff.generator.tree.TreeGenerator;
import de.domisum.exziff.generator.tree.TreeGeneratorContext;

import java.util.Random;

public class TreeGeneratorOakRegular implements TreeGenerator
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


		// INIT
		public GenerateMethodObject(TreeGeneratorContext treeGeneratorContext, long seed)
		{
			this.treeGeneratorContext = treeGeneratorContext;
			random = new Random(seed);
		}


		// GENERATE
		public BlockStructure generate()
		{


			return blockStructure;
		}

	}
}
