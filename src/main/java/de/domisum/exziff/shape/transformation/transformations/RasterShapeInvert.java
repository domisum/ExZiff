package de.domisum.exziff.shape.transformation.transformations;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeInvert extends RasterShapeTransformation
{

	// -------
	// TRANSFORMATION
	// -------
	@Override
	public RasterShape transform(RasterShape input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				pixels[y][x] = !input.get(x, y);

		return new RasterShape(pixels);
	}

}
