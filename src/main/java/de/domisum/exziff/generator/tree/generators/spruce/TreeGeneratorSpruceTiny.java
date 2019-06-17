package de.domisum.exziff.generator.tree.generators.spruce;

import de.domisum.exziff.blocksource.BlockSource;
import de.domisum.exziff.blocksource.sources.BlockSourceConstant;
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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TreeGeneratorSpruceTiny implements TreeGenerator
{

	@Override
	public BlockStructure generate(long seed, TreeGeneratorContext treeGeneratorContext)
	{
		return new GenerateMethodObject(treeGeneratorContext, seed).generate();
	}

	private static class GenerateMethodObject
	{

		private static final BlockSource LOG_SOURCE = new BlockSourceConstant(new BlockBuilder(Material.LOG_SPRUCE)
				.set(Axis.Y)
				.build());
		private static final BlockSource LEAVES_SOURCE = new BlockSourceConstant(new BlockBuilder(Material.LEAVES_SPRUCE).build());

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
			double initialRadius = 0.4;

			double currentRadius = initialRadius;
			TreeBranch mainBranch = new TreeBranch();
			Set<TreeBranch> minorBranches = new HashSet<>();
			for(double h = 0.5; currentRadius > 0.1; h++)
			{
				BlockSource blockSource = (currentRadius < 0.18) ? LEAVES_SOURCE : LOG_SOURCE;
				Vector3D position = new Vector3D(0.5, h, 0.5);

				mainBranch.addNode(position, currentRadius, blockSource);

				if(h > 2)
					minorBranches.addAll(generateBranchesAtHeight(h, currentRadius));

				currentRadius *= RandomUtil.getFromRange(0.8, 0.9, random);
			}

			blockStructure.set(mainBranch.render());
			for(TreeBranch minorBranch : minorBranches)
				blockStructure.setIfNotSetAlready(minorBranch.render());

			return blockStructure;
		}

		private Set<TreeBranch> generateBranchesAtHeight(double y, double mainBranchRadius)
		{
			Set<TreeBranch> treeBranches = new HashSet<>();

			for(double deg = 0; deg <= 360; deg += RandomUtil.getFromRange(30, 40))
				treeBranches.add(generateBranchAtHeightAndAngle(y, mainBranchRadius, deg));

			return treeBranches;
		}

		private TreeBranch generateBranchAtHeightAndAngle(double y, double mainBranchRadius, double angleDeg)
		{
			Vector3D upwards = new Vector3D(0, 1, 0);
			Vector3D branchDirection = VectorUtil.getVectorRotatedAround(upwards,
					Math.toRadians(RandomUtil.getFromRange(80, 90)),
					Math.toRadians(angleDeg)
			);

			Vector3D branchStartPosition = new Vector3D(0.5, y, 0.5);

			Vector3D currentPosition = branchStartPosition;
			TreeBranch branch = new TreeBranch();
			double currentRadius = (mainBranchRadius/3)+0.07;
			while(currentRadius > 0.1)
			{
				BlockSource blockSource = (currentRadius < 0.18) ? LEAVES_SOURCE : LOG_SOURCE;
				branch.addNode(currentPosition, currentRadius, blockSource);

				currentPosition = currentPosition.add(branchDirection);
				currentRadius *= RandomUtil.getFromRange(0.8, 0.85, random);
			}

			return branch;
		}

	}
}
