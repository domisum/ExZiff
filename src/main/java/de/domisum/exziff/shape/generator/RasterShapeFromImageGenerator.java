package de.domisum.exziff.shape.generator;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;

public class RasterShapeFromImageGenerator extends RasterShapeGenerator
{

	// INPUT
	private BufferedImage image;

	// SETTINGS
	private double threshold;


	// -------
	// INITIALIZATION
	// -------
	public RasterShapeFromImageGenerator(int width, int height, BufferedImage image, double threshold)
	{
		super(width, height);

		if(image == null)
			throw new IllegalArgumentException("The image can't be null");

		if(threshold < 0 || threshold > 1)
			throw new IllegalArgumentException("The threshold has to be between 0.0 and 1.0");

		this.image = image;
		this.threshold = threshold;
	}


	// -------
	// GENERATION
	// -------
	@Override
	public RasterShape generate()
	{
		int[][] colorPixels = ImageUtil.getPixels(this.image);

		boolean[][] pixels = new boolean[this.height][this.width];
		for(int y = 0; y < this.height; y++)
			for(int x = 0; x < this.width; x++)
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

		return new RasterShape(pixels);
	}

}
