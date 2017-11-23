package de.domisum.exziff.map.generator.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.Generator;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;

public class BooleanMapFromImageGenerator implements Generator<BufferedImage, BooleanMap>
{

	// SETTINGS
	private double threshold;


	// INITIALIZATION
	public BooleanMapFromImageGenerator(double threshold)
	{
		if(threshold < 0 || threshold > 1)
			throw new IllegalArgumentException("The threshold has to be between 0.0 and 1.0");

		this.threshold = threshold;
	}


	// GENERATION
	@Override public BooleanMap generate(BufferedImage image)
	{
		int[][] colorPixels = ImageUtil.getPixels(image);

		boolean[][] pixels = new boolean[image.getHeight()][image.getWidth()];
		for(int y = 0; y < image.getHeight(); y++)
			for(int x = 0; x < image.getWidth(); x++)
			{
				int color = colorPixels[y][x];
				int blue = color&0xff;
				int green = (color>>8)&0xff;
				int red = (color>>16)&0xff;

				double sum = blue+green+red;
				double proportion = sum/(256*3);

				if(proportion >= this.threshold)
					pixels[y][x] = true;
			}

		return new BooleanMap(pixels);
	}

}
