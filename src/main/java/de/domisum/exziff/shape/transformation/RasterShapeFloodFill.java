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

		Queue<PixelCoordinates> unvisitedPixels = new LinkedList<>();
		boolean[][] blockedPixels = new boolean[input.getHeight()][input.getWidth()];

		// start floodfill from the outer edge
		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				if(x == 0 || y == 0 || x == input.getWidth()-1 || y == input.getHeight()-1)
					if(!input.get(x, y))
					{
						unvisitedPixels.add(new PixelCoordinates(x, y));
						blockedPixels[y][x] = true;
					}

		while(unvisitedPixels.size() > 0)
		{
			PixelCoordinates coordinates = unvisitedPixels.poll();
			unvisitedPixels.remove(coordinates);

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
