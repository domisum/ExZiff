package de.domisum.exziff.generator.util;

import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.lib.auxilium.data.container.math.Vector2D;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * https://www.cs.ubc.ca/~rbridson/docs/bridson-siggraph07-poissondisk.pdf
 */
public class PoissonDiskPointGenerator implements RandomizedGeneratorOneInput<Double, Set<Vector2D>>
{

	@Override
	public Set<Vector2D> generate(long seed, Double radius)
	{
		Random random = new Random(seed);

		double cellSize = radius/Math.sqrt(2);
		int gridSize = (int) Math.ceil(1/cellSize);
		Vector2D[][] grid = new Vector2D[gridSize][gridSize];

		LinkedList<Vector2D> active = new LinkedList<>();
		Set<Vector2D> finalized = new HashSet<>();

		Vector2D startPoint = new Vector2D(random.nextDouble(), random.nextDouble());
		active.add(startPoint);
		grid[(int) Math.floor(startPoint.getY()/cellSize)][(int) Math.floor(startPoint.getX()/cellSize)] = startPoint;

		while(!active.isEmpty())
		{
			Vector2D currentPoint = active.removeFirst();

			for(int i = 0; i < 30; i++)
			{
				double angle = random.nextDouble()*2*Math.PI;
				double distance = (random.nextDouble()*radius)+radius;
				Vector2D offset = new Vector2D(Math.cos(angle), Math.sin(angle)).multiply(distance);
				Vector2D newPoint = currentPoint.add(offset);

				if(isOutOfBounds(newPoint))
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

	private boolean isOutOfBounds(Vector2D newPoint)
	{
		boolean xOutOfBounds = (newPoint.getX() < 0) || (newPoint.getX() >= 1);
		boolean yOutOfBounds = (newPoint.getY() < 0) || (newPoint.getY() >= 1);

		return xOutOfBounds || yOutOfBounds;
	}

	private boolean isOutOfBounds(int gridSize, int x, int y)
	{
		boolean xOutOfBounds = (x < 0) || (x >= gridSize);
		boolean yOutOfBounds = (y < 0) || (y >= gridSize);

		return xOutOfBounds || yOutOfBounds;
	}

}
