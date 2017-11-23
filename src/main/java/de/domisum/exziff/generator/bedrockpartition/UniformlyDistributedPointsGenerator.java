package de.domisum.exziff.generator.bedrockpartition;

import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UniformlyDistributedPointsGenerator implements RandomizedGeneratorOneInput<Integer, Set<Vector2D>>
{

	// SETTINGS
	@Getter @Setter private int desiredNumberOfPoints = 300;


	// GENERATE
	@Override public Set<Vector2D> generate(long seed, Integer desiredNumberOfPoints)
	{
		return new GenerateMethodObject(new Random(seed), desiredNumberOfPoints).generate();
	}

	@RequiredArgsConstructor
	private class GenerateMethodObject
	{

		// INPUT
		private final Random random;
		private final int desiredNumberOfPoints;

		// POINTS
		private Set<Vector2D> points = new HashSet<>();


		// GENERATE
		public Set<Vector2D> generate()
		{
			generateBasePoints();
			balancePoints();

			return points;
		}

		private void generateBasePoints()
		{
			while(points.size() < desiredNumberOfPoints)
			{
				double pointX = RandomUtil.getFromRange(0, 1, random);
				double pointY = RandomUtil.getFromRange(0, 1, random);
				Vector2D point = new Vector2D(pointX, pointY);

				points.add(point);
			}
		}

		private void balancePoints()
		{
			for(int i = 0; i < 5; i++)
				balancePointsIteration();
		}

		private void balancePointsIteration()
		{
			// TODO
		}

	}

}
