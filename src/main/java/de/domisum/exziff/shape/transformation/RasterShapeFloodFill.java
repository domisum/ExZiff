package de.domisum.exziff.shape.transformation;

import de.domisum.exziff.shape.PixelCoordinates;
import de.domisum.exziff.shape.RasterShape;

import java.util.HashSet;
import java.util.Set;

public class RasterShapeFloodFill extends RasterShapeTransformation
{

	// -------
	// TRANSFORMATION
	// -------
	@Override
	public RasterShape transform(RasterShape input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		Set<PixelCoordinates> unvisitedPixels = new HashSet<>();
		Set<PixelCoordinates> visitedPixels = new HashSet<>();

		// start floodfill from the outer edge
		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				if(x == 0 || y == 0 || x == input.getWidth()-1 || y == input.getHeight()-1)
					unvisitedPixels.add(new PixelCoordinates(x, y));

		while(unvisitedPixels.size() > 0)
		{
			if(Math.random() < 0.01)
				System.out.println(unvisitedPixels.size());

			PixelCoordinates coordinates = unvisitedPixels.iterator().next();
			unvisitedPixels.remove(coordinates);
			visitedPixels.add(coordinates);

			if(coordinates.x < 0 || coordinates.y < 0 || coordinates.x >= input.getWidth() || coordinates.y >= input.getHeight())
				continue;

			if(input.get(coordinates.x, coordinates.y))
				continue;

			pixels[coordinates.y][coordinates.x] = true;

			tryAddNeighborPixel(coordinates, unvisitedPixels, visitedPixels, 1, 0);
			tryAddNeighborPixel(coordinates, unvisitedPixels, visitedPixels, -1, 0);
			tryAddNeighborPixel(coordinates, unvisitedPixels, visitedPixels, 0, 1);
			tryAddNeighborPixel(coordinates, unvisitedPixels, visitedPixels, 0, -1);
		}

		return new RasterShape(pixels);
	}

	private void tryAddNeighborPixel(PixelCoordinates base, Set<PixelCoordinates> unvisitedPixels,
			Set<PixelCoordinates> visitedPixels, int dX, int dY)
	{
		PixelCoordinates newCoordinates = new PixelCoordinates(base.x+dX, base.y+dY);
		if(visitedPixels.contains(newCoordinates))
			return;

		unvisitedPixels.add(newCoordinates);
	}

}
