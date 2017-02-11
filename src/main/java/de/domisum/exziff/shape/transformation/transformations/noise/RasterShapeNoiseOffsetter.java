package de.domisum.exziff.shape.transformation.transformations.noise;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.transformation.transformations.RasterShapeTransformation;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;

public class RasterShapeNoiseOffsetter extends RasterShapeTransformation
{

	// SETTINS
	private LayeredOpenSimplexNoise noiseX;
	private LayeredOpenSimplexNoise noiseY;

	private double magnifier;


	// -------
	// INIT
	// -------
	public RasterShapeNoiseOffsetter(LayeredOpenSimplexNoise noiseX, LayeredOpenSimplexNoise noiseY, double magnifier)
	{
		this.noiseX = noiseX;
		this.noiseY = noiseY;

		this.magnifier = magnifier;
	}


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
				if(!input.get(x, y))
					continue;

				double scale = input.getWidth();

				double dX = this.noiseX.evaluate(x/scale, y/scale)*this.magnifier;
				double dY = this.noiseY.evaluate(x/scale, y/scale)*this.magnifier;

				int nX = x+(int) Math.round(dX);
				int nY = y+(int) Math.round(dY);

				if(nX < 0 || nY < 0 || nX >= input.getWidth() || nY >= input.getHeight())
					continue;

				pixels[nY][nX] = true;
			}

		return new RasterShape(pixels);
	}

}
