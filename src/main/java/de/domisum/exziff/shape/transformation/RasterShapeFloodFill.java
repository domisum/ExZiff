package de.domisum.exziff.shape.transformation;

import de.domisum.exziff.shape.PixelCoordinates;
import de.domisum.exziff.shape.RasterShape;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class RasterShapeFloodFill extends RasterShapeTransformation
{

	// -------
	// TRANSFORMATION
	// -------
	@Override
	public RasterShape transform(RasterShape input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		// these pixels have yet to be processed
		Queue<PixelCoordinates> unvisitedPixels = new LinkedList<>();
		// the pixels marked with true should not be added to the unvisitedPixels queue again
		boolean[][] blockedPixels = new boolean[input.getHeight()][input.getWidth()];

		// start floodfill from the outer edge
		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				if(x == 0 || y == 0 || x == input.getWidth()-1 || y == input.getHeight()-1) // only the pixels on the outer edge
					if(!input.get(x, y)) // and only if they are not blocked themselves
					{
						unvisitedPixels.add(new PixelCoordinates(x, y));
						blockedPixels[y][x] = true;
					}

		while(unvisitedPixels.size() > 0)
		{
			PixelCoordinates coordinates = unvisitedPixels.poll();
			pixels[coordinates.y][coordinates.x] = true;

			tryAddNeighborPixel(input, coordinates, unvisitedPixels, blockedPixels, 1, 0);
			tryAddNeighborPixel(input, coordinates, unvisitedPixels, blockedPixels, -1, 0);
			tryAddNeighborPixel(input, coordinates, unvisitedPixels, blockedPixels, 0, 1);
			tryAddNeighborPixel(input, coordinates, unvisitedPixels, blockedPixels, 0, -1);
		}

		return new RasterShape(pixels);
	}

	private void tryAddNeighborPixel(RasterShape input, PixelCoordinates base, Collection<PixelCoordinates> unvisitedPixels,
			boolean[][] blockedPixels, int dX, int dY)
	{
		int nX = base.x+dX;
		int nY = base.y+dY;

		if(nX < 0 || nY < 0 || nX >= input.getWidth() || nY >= input.getHeight())
			return;

		if(blockedPixels[nY][nX])
			return;
		blockedPixels[nY][nX] = true;

		if(input.get(nX, nY))
			return;

		unvisitedPixels.add(new PixelCoordinates(base.x+dX, base.y+dY));
	}

}
