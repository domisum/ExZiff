package de.domisum.exziff.map.transformation.bool.noise;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.transformation.bool.BooleanMapTransformation;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;

public class BooleanMapNoiseOffsetter extends BooleanMapTransformation
{

	// SETTINGS
	private LayeredOpenSimplexNoise noiseX;
	private LayeredOpenSimplexNoise noiseY;


	// -------
	// INIT
	// -------
	public BooleanMapNoiseOffsetter(LayeredOpenSimplexNoise noiseX, LayeredOpenSimplexNoise noiseY)
	{
		this.noiseX = noiseX;
		this.noiseY = noiseY;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override public BooleanMap transform(BooleanMap input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		for(int x = 0; x < input.getWidth(); x++)
			for(int y = 0; y < input.getHeight(); y++)
			{
				if(!input.get(x, y))
					continue;

				double scale = input.getWidth();

				double dX = this.noiseX.evaluate(x/scale, y/scale);
				double dY = this.noiseY.evaluate(x/scale, y/scale);

				int nX = x+(int) Math.round(dX);
				int nY = y+(int) Math.round(dY);

				if(nX < 0 || nY < 0 || nX >= input.getWidth() || nY >= input.getHeight())
					continue;

				pixels[nY][nX] = true;
			}

		return new BooleanMap(pixels);
	}

}
