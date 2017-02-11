package de.domisum.exziff.shape.transformer.deformer;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.transformer.RasterShapeTransformer;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;

public class RasterShapeTransformerNoiseOffsetter extends RasterShapeTransformer
{

	// SETTINS
	private LayeredOpenSimplexNoise noiseX;
	private LayeredOpenSimplexNoise noiseY;

	private double magnifier;


	// -------
	// INIT
	// -------
	public RasterShapeTransformerNoiseOffsetter(RasterShape input, LayeredOpenSimplexNoise noiseX, LayeredOpenSimplexNoise noiseY,
			double magnifier)
	{
		super(input);

		this.noiseX = noiseX;
		this.noiseY = noiseY;

		this.magnifier = magnifier;
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
				if(!this.input.get(x, y))
					continue;

				double scale = this.input.getWidth();

				double dX = this.noiseX.evaluate(x/scale, y/scale)*this.magnifier;
				double dY = this.noiseY.evaluate(x/scale, y/scale)*this.magnifier;

				int nX = x+(int) Math.round(dX);
				int nY = y+(int) Math.round(dY);

				if(nX < 0 || nY < 0 || nX >= this.input.getWidth() || nY >= this.input.getHeight())
					continue;

				pixels[nY][nX] = true;
			}

		this.output = new RasterShape(pixels);
	}

}
