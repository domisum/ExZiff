package de.domisum.exziff.generator.tree.generators.oak;

import de.domisum.exziff.blocksource.BlockSource;
import de.domisum.exziff.blocksource.sources.BlockSourceConstant;
import de.domisum.exziff.blocksource.sources.BlockSourceRandomized.BlockSourceRandomizedBuilder;
import de.domisum.exziff.blockstructure.BlockStructure;
import de.domisum.exziff.generator.tree.TreeGenerator;
import de.domisum.exziff.generator.tree.TreeGeneratorContext;
import de.domisum.exziff.generator.tree.components.TreeBranch;
import de.domisum.exziff.world.block.Axis;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import de.domisum.lib.auxilium.util.math.VectorUtil;

import java.util.ArrayList;
import java.util.List;
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

		private static final BlockSource LOG_SOURCE = new BlockSourceConstant(new BlockBuilder(Material.LOG_OAK)
				.set(Axis.NONE)
				.build());
		private final BlockSource leavesSource;

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

			leavesSource = new BlockSourceRandomizedBuilder(random.nextLong())
					.putBlock(new BlockBuilder(Material.LEAVES_OAK).build(), 0.6)
					.putBlock(null, 0.4)
					.build();
		}


		// GENERATE
		public BlockStructure generate()
		{
			Vector3D startPosition = new Vector3D(0.5, 0.5, 0.5);
			Vector3D startDirection = new Vector3D(0, 1, 0);
			double startRadius = 1.2;
			generateAndBuildBranch(startPosition, startDirection, startRadius);

			return blockStructure;
		}

		private void generateAndBuildBranch(Vector3D startPosition, Vector3D startDirection, double startRadius)
		{
			List<TreeBranch> treeBranches = generateBranch(startPosition, startDirection, startRadius);

			for(TreeBranch treeBranch : treeBranches)
				blockStructure.setIfNotSetAlready(treeBranch.render());
		}

		private List<TreeBranch> generateBranch(Vector3D startPosition, Vector3D startDirection, double startRadius)
		{
			TreeBranch currentBranch = new TreeBranch();
			List<TreeBranch> branches = new ArrayList<>();
			branches.add(currentBranch);

			Vector3D currentPosition = startPosition;
			Vector3D currentDirection = startDirection;
			double currentRadius = startRadius;
			while(true)
			{
				if(shouldSplit(currentPosition))
				{
					double splitRadius = currentRadius*0.7;
					break;
				}

				currentBranch.addNode(currentPosition, currentRadius, LOG_SOURCE);

				currentPosition = currentPosition.add(currentDirection);
				currentDirection = adjustDirection(currentPosition, currentDirection);
				currentRadius *= RandomUtil.getFromRange(0.85, 0.95);
			}

			return branches;
		}

		private boolean shouldSplit(Vector3D position)
		{
			if(position.getY() < 7)
				return false;

			return RandomUtil.getByChance(0.5, random);
		}

		private Vector3D adjustDirection(Vector3D position, Vector3D direction)
		{
			double angleBetweenVectorsRad = Math.toRadians(RandomUtil.getFromRange(0, 15, random));
			double rotationAngleRad = Math.toRadians(RandomUtil.getFromRange(0, 360, random));

			return VectorUtil.getVectorRotatedAround(direction, angleBetweenVectorsRad, rotationAngleRad);
		}

	}
}
