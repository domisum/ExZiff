package de.domisum.exziff.map.transformation;

import de.domisum.exziff.map.ShapeMap;

public class ShapeMapMapInvert extends ShapeMapTransformation
{

	// -------
	// TRANSFORMATION
	// -------
	@Override
	public ShapeMap transform(ShapeMap input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				pixels[y][x] = !input.get(x, y);

		return new ShapeMap(pixels);
	}

}
