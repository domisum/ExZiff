package de.domisum.exziff.shape.transformation;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeSmooth extends RasterShapeTransformation
{

	// SETTINGS
	private final int radius;
	private final double removeThreshold;
	private final double addThreshold;


	// -------
	// INIT
	// -------
	public RasterShapeSmooth(int radius, double removeThreshold, double addThreshold)
	{
		if(radius <= 0)
			throw new IllegalArgumentException("The radius has to be at least 1");

		if(removeThreshold < 0 || removeThreshold > 1)
			throw new IllegalArgumentException("The removeThreshold has to be between 0 and 1");
		if(addThreshold < 0 || addThreshold > 1)
			throw new IllegalArgumentException("The addThreshold has to be between 0 and 1");

		this.radius = radius;
		this.removeThreshold = removeThreshold;
		this.addThreshold = addThreshold;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public RasterShape transform(RasterShape input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		int diameter = (this.radius+1+this.radius);

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
			{
				double sum = 0;
				for(int rY = -this.radius; rY <= this.radius; rY++)
					for(int rX = -this.radius; rX <= this.radius; rX++)
					{
						int cX = x+rX;
						int cY = y+rY;

						if(cX < 0 || cY < 0 || cX >= input.getWidth() || cY >= input.getHeight())
							continue;
						if(input.get(cX, cY))
							sum++;
					}

				double proportion = sum/diameter*diameter;

				// keep old true-pixels
				if(input.get(x, y))
				{
					if(proportion >= this.removeThreshold)
						pixels[y][x] = true;
				}
				else // add new pixels
				{
					if(proportion >= this.addThreshold)
						pixels[y][x] = true;
				}
			}

		return new RasterShape(pixels);
	}

}
