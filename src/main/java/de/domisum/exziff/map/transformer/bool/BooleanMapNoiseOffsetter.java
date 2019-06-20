package de.domisum.exziff.map.transformer.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.transformer.Transformer;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;


public class BooleanMapNoiseOffsetter implements Transformer<BooleanMap>
{

	// SETTINGS
	private final LayeredOpenSimplexNoise noiseX;
	private final LayeredOpenSimplexNoise noiseY;


	// INIT

	/**
	 * Offsets pixels by the ammount provided by the noise functions. The noise functions are given relative coordinates
	 * and should give relative coordinates back. This means that if the noise functions return an offset of 0.1, the pixel
	 * will be offset by 0.1 times the size of the map pixels.
	 *
	 * @param noiseX the noise function dictating x offset
	 * @param noiseY the noise function dictating y offset
	 */
	public BooleanMapNoiseOffsetter(LayeredOpenSimplexNoise noiseX, LayeredOpenSimplexNoise noiseY)
	{
		this.noiseX = noiseX;
		this.noiseY = noiseY;
	}


	// TRANSFORMATION
	@Override
	public BooleanMap transform(BooleanMap input)
	{
		double scale = input.getWidth();

		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];
		for(int x = 0; x < input.getWidth(); x++)
			for(int y = 0; y < input.getHeight(); y++)
			{
				if(!input.get(x, y)) // only offset true/white pixels
					continue;

				double dX = noiseX.evaluate(x/scale, y/scale)*scale;
				double dY = noiseY.evaluate(x/scale, y/scale)*scale;

				int nX = x+(int) Math.round(dX);
				int nY = y+(int) Math.round(dY);

				if(nX < 0 || nY < 0 || nX >= input.getWidth() || nY >= input.getHeight())
					continue;

				pixels[nY][nX] = true;
			}

		return new BooleanMap(pixels);
	}

}
