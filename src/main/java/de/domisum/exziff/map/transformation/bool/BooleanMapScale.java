package de.domisum.exziff.map.transformation.bool;

import de.domisum.exziff.map.BooleanMap;

public class BooleanMapScale extends BooleanMapTransformation
{

	// SETTINGS
	private double scalingFactor;


	// -------
	// INITIALIZATION
	// -------
	public BooleanMapScale(double scalingFactor)
	{
		if(scalingFactor <= 0)
			throw new IllegalArgumentException("The scaling factor has to be positive");

		this.scalingFactor = scalingFactor;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public BooleanMap transform(BooleanMap input)
	{
		int nWidth = (int) Math.floor(input.getWidth()*this.scalingFactor);
		int nHeight = (int) Math.floor(input.getHeight()*this.scalingFactor);

		if(nWidth == 0 || nHeight == 0)
			throw new IllegalArgumentException(
					"The scaling factor "+this.scalingFactor+" is too small (output would either have height or width 0)");

		boolean[][] pixels = new boolean[nHeight][nWidth];
		for(int x = 0; x < nWidth; x++)
			for(int y = 0; y < nHeight; y++)
			{
				// TODO this technique may skip pixels when upscaling and therefore may be inaccurate
				int inputX = (int) Math.floor(x/this.scalingFactor);
				int inputY = (int) Math.floor(y/this.scalingFactor);

				pixels[y][x] = input.get(inputX, inputY);
			}

		return new BooleanMap(pixels);
	}

}
