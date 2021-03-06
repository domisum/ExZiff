package de.domisum.exziff.map.generator.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.lib.auxilium.contracts.generator.Generator;

import java.awt.image.BufferedImage;

public class BooleanMapFromImageGenerator implements Generator<BufferedImage, BooleanMap>
{

	// SETTINGS
	private final double threshold;


	// INITIALIZATION
	public BooleanMapFromImageGenerator(double threshold)
	{
		if((threshold < 0) || (threshold > 1))
			throw new IllegalArgumentException("The threshold has to be between 0.0 and 1.0");

		this.threshold = threshold;
	}


	// GENERATION
	@Override
	public BooleanMap generate(BufferedImage image)
	{
		boolean[][] pixels = new boolean[image.getHeight()][image.getWidth()];
		for(int y = 0; y < image.getHeight(); y++)
			for(int x = 0; x < image.getWidth(); x++)
			{
				int color = image.getRGB(x, y);
				int blue = color&0xff;
				int green = (color >> 8)&0xff;
				int red = (color >> 16)&0xff;

				double sum = blue+green+red;
				double proportion = sum/(256*3);

				if(proportion >= threshold)
					pixels[y][x] = true;
			}

		return new BooleanMap(pixels);
	}

}
