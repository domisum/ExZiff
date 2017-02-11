package de.domisum.exziff.shape.transformer;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeTransformerShift extends RasterShapeTransformer
{

	// SETTINGS
	private int dX;
	private int dY;


	// -------
	// INIT
	// -------
	public RasterShapeTransformerShift(RasterShape input, int dX, int dY)
	{
		super(input);

		this.dX = dX;
		this.dY = dY;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public void transform()
	{
		boolean[][] pixels = new boolean[this.input.getHeight()][this.input.getWidth()];

		for(int x = 0; x < this.input.getWidth(); x++)
			for(int y = 0; y < this.input.getHeight(); y++)
			{
				int nX = x+this.dX;
				int nY = y+this.dY;

				if(nX < 0 || nY < 0 || nX > this.input.getWidth() || nY > this.input.getHeight())
					continue;

				pixels[nY][nX] = this.input.get(x, y);
			}

		this.output = new RasterShape(pixels);
	}

}
