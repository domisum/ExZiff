package de.domisum.exziff.shape.transformer;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeTransformerInvert extends RasterShapeTransformer
{

	// -------
	// INITIALIZATION
	// -------
	public RasterShapeTransformerInvert(RasterShape input)
	{
		super(input);
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public void transform()
	{
		boolean[][] pixels = new boolean[this.input.getHeight()][this.input.getWidth()];

		for(int y = 0; y < this.input.getHeight(); y++)
			for(int x = 0; x < this.input.getWidth(); x++)
				pixels[y][x] = !this.input.get(x, y);

		this.output = new RasterShape(pixels);
	}

}
