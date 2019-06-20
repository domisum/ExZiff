package de.domisum.exziff.generator.util;

import de.domisum.exziff.generator.RandomizedGeneratorTwoInputs;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * https://www.cs.ubc.ca/~rbridson/docs/bridson-siggraph07-poissondisk.pdf
 */
public class PoissonDiskPointGenerator implements RandomizedGeneratorTwoInputs<Double, Double, Set<Vector2D>>
{

	@Override
	public Set<Vector2D> generate(long seed, Double radius, Double minDistanceToEdge)
	{
		Random random = new Random(seed);

		double cellSize = radius/Math.sqrt(2);
		int gridSize = (int) Math.ceil(1/cellSize);
		Vector2D[][] grid = new Vector2D[gridSize][gridSize];

		LinkedList<Vector2D> active = new LinkedList<>();
		Set<Vector2D> finalized = new HashSet<>();

		double startPointX = RandomUtil.getFromRange(minDistanceToEdge, 1-minDistanceToEdge, random);
		double startPointY = RandomUtil.getFromRange(minDistanceToEdge, 1-minDistanceToEdge, random);
		Vector2D startPoint = new Vector2D(startPointX, startPointY);
		active.add(startPoint);
		grid[(int) Math.floor(startPoint.getY()/cellSize)][(int) Math.floor(startPoint.getX()/cellSize)] = startPoint;

		while(!active.isEmpty())
		{
			Vector2D currentPoint = active.removeFirst();

			for(int i = 0; i < 1000; i++)
			{
				double angle = random.nextDouble()*2*Math.PI;
				double distance = (random.nextDouble()*radius)+radius;
				Vector2D offset = new Vector2D(Math.cos(angle), Math.sin(angle)).multiply(distance);
				Vector2D newPoint = currentPoint.add(offset);

				if(isOutOfBounds(newPoint, minDistanceToEdge))
					continue;

				int newPointCellX = (int) Math.floor(newPoint.getX()/cellSize);
				int newPointCellY = (int) Math.floor(newPoint.getY()/cellSize);

				boolean ok = true;
				for(int dX = -2; dX <= 2; dX++)
					for(int dY = -2; dY <= 2; dY++)
					{
						int x = newPointCellY+dY;
						int y = newPointCellX+dX;
						if(isOutOfBounds(gridSize, x, y))
							continue;

						Vector2D p = grid[x][y];
						if(p == null)
							continue;

						if(newPoint.distanceTo(p) < radius)
							ok = false;
					}

				if(ok)
				{
					active.add(newPoint);
					grid[newPointCellY][newPointCellX] = newPoint;
				}
			}

			finalized.add(currentPoint);
		}

		return finalized;
	}

	private boolean isOutOfBounds(Vector2D newPoint, double minDistanceToEdge)
	{
		boolean xOutOfBounds = (newPoint.getX() < minDistanceToEdge) || (newPoint.getX() >= (1-minDistanceToEdge));
		boolean yOutOfBounds = (newPoint.getY() < minDistanceToEdge) || (newPoint.getY() >= (1-minDistanceToEdge));

		return xOutOfBounds || yOutOfBounds;
	}

	private boolean isOutOfBounds(int gridSize, int x, int y)
	{
		boolean xOutOfBounds = (x < 0) || (x >= gridSize);
		boolean yOutOfBounds = (y < 0) || (y >= gridSize);

		return xOutOfBounds || yOutOfBounds;
	}

}
