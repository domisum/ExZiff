package de.domisum.exziff.map.transformation.bool;

import de.domisum.exziff.map.BooleanMap;

public class BooleanMapSmooth extends BooleanMapTransformation
{

	// SETTINGS
	private final int radius;
	private final double removeThreshold;
	private final double addThreshold;
	private final int iterations;


	// -------
	// INIT
	// -------
	public BooleanMapSmooth(int radius, double removeThreshold, double addThreshold)
	{
		this(radius, removeThreshold, addThreshold, 1);
	}

	public BooleanMapSmooth(int radius, double removeThreshold, double addThreshold, int iterations)
	{
		if(radius <= 0)
			throw new IllegalArgumentException("The radius has to be at least 1");

		if(removeThreshold < 0 || removeThreshold > 1)
			throw new IllegalArgumentException("The removeThreshold has to be between 0 and 1");
		if(addThreshold < 0 || addThreshold > 1)
			throw new IllegalArgumentException("The addThreshold has to be between 0 and 1");

		if(removeThreshold > addThreshold)
			throw new IllegalArgumentException("The removeThreshold has to be smaller than the addThreshold");

		if(iterations <= 0)
			throw new IllegalArgumentException("The number of iterations has to be greater than 0");


		this.radius = radius;
		this.removeThreshold = removeThreshold;
		this.addThreshold = addThreshold;
		this.iterations = iterations;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public BooleanMap transform(BooleanMap input)
	{
		BooleanMap deformedShape = input;
		for(int i = 0; i < this.iterations; i++)
			deformedShape = smooth(deformedShape);

		return deformedShape;
	}

	private BooleanMap smooth(BooleanMap input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		int diameter = (this.radius+1+this.radius);

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
			{
				double sum = 0;
				for(int relY = -this.radius; relY <= this.radius; relY++)
					for(int relX = -this.radius; relX <= this.radius; relX++)
					{
						int cX = x+relX;
						int cY = y+relY;

						if(cX < 0 || cY < 0 || cX >= input.getWidth() || cY >= input.getHeight()) // in bounds?
							continue;

						if(input.get(cX, cY))
							sum++;
					}

				double proportion = sum/(diameter*diameter);

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

		return new BooleanMap(pixels);
	}

}
