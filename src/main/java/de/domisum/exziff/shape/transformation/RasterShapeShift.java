package de.domisum.exziff.shape.transformation;

import de.domisum.exziff.shape.RasterShape;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RasterShapeShift extends RasterShapeTransformation
{

	// SETTINGS
	private int dX;
	private int dY;


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public RasterShape transform(RasterShape input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		for(int x = 0; x < input.getWidth(); x++)
			for(int y = 0; y < input.getHeight(); y++)
			{
				int nX = x+this.dX;
				int nY = y+this.dY;

				if(nX < 0 || nY < 0 || nX >= input.getWidth() || nY >= input.getHeight())
					continue;

				pixels[nY][nX] = input.get(x, y);
			}

		return new RasterShape(pixels);
	}

}
