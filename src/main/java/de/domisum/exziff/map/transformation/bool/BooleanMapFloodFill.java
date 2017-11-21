package de.domisum.exziff.map.transformation.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.lib.auxilium.data.container.math.Coordinate2DInt;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class BooleanMapFloodFill extends BooleanMapTransformation
{

	// TRANSFORMATION
	@Override public BooleanMap transform(BooleanMap input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		// these pixels have yet to be processed
		Queue<Coordinate2DInt> unvisitedPixels = new LinkedList<>();
		// the pixels marked with true have already been processend and can be ignored
		boolean[][] processedPixels = new boolean[input.getHeight()][input.getWidth()];

		// start floodfill from the outer edge
		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				if(x == 0 || y == 0 || x == input.getWidth()-1 || y == input.getHeight()-1) // only the pixels on the outer edge
					if(!input.get(x, y)) // and only if they are not blocked themselves
					{
						unvisitedPixels.add(new Coordinate2DInt(x, y));
						processedPixels[y][x] = true;
					}

		while(unvisitedPixels.size() > 0)
		{
			Coordinate2DInt coordinates = unvisitedPixels.poll();
			pixels[coordinates.y][coordinates.x] = true;

			// check all neighbors in 4 directions
			tryAddNeighborPixel(input, coordinates, unvisitedPixels, processedPixels, 1, 0);
			tryAddNeighborPixel(input, coordinates, unvisitedPixels, processedPixels, -1, 0);
			tryAddNeighborPixel(input, coordinates, unvisitedPixels, processedPixels, 0, 1);
			tryAddNeighborPixel(input, coordinates, unvisitedPixels, processedPixels, 0, -1);
		}

		return new BooleanMap(pixels);
	}

	private void tryAddNeighborPixel(BooleanMap input, Coordinate2DInt base, Collection<Coordinate2DInt> unvisitedPixels,
			boolean[][] processedPixels, int dX, int dY)
	{
		int nX = base.x+dX;
		int nY = base.y+dY;

		if(nX < 0 || nY < 0 || nX >= input.getWidth() || nY >= input.getHeight()) // in bounds?
			return;

		if(processedPixels[nY][nX]) // don't add to the queue if they have already been marked as processed
			return;

		// mark this pixel as processed, no matter if it is going to be added to the queue or not,
		// since it won't be of use in any iteration
		processedPixels[nY][nX] = true;

		if(input.get(nX, nY)) // don't add the pixel to the queue if the pixel is blocked
			return;

		unvisitedPixels.add(new Coordinate2DInt(base.x+dX, base.y+dY));
	}

}
