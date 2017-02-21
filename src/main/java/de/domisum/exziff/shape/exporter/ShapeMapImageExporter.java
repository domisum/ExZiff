package de.domisum.exziff.shape.exporter;

import de.domisum.exziff.shape.ShapeMap;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;

public class ShapeMapImageExporter extends ShapeMapExporter<BufferedImage>
{

	// CONSTANTS
	private static final int COLOR_TRUE = 0xffffff;
	private static final int COLOR_FALSE = 0x000000;


	// -------
	// EXPORT
	// -------
	@Override
	public BufferedImage export(ShapeMap input)
	{
		int[][] pixels = new int[input.getHeight()][input.getWidth()];

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
			{
				int color = input.get(x, y) ? COLOR_TRUE : COLOR_FALSE;

				pixels[y][x] = color;
			}

		return ImageUtil.getImageFromPixels(pixels);
	}

}