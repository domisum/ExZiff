package de.domisum.exziff.generator.tree;

import de.domisum.exziff.blockstructure.BlockStructure;

public interface TreeGenerator
{

	BlockStructure generate(long seed, TreeGeneratorContext context);

}
