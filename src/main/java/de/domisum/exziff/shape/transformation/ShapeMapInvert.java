package de.domisum.exziff.shape.transformation;

import de.domisum.exziff.shape.ShapeMap;

public class ShapeMapInvert extends ShapeMapTransformation
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
