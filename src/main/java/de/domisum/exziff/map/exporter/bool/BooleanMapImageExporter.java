package de.domisum.exziff.map.exporter.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;

public class BooleanMapImageExporter extends BooleanMapExporter<BufferedImage>
{

	// CONSTANTS
	private static final int COLOR_TRUE = 0xffffff;
	private static final int COLOR_FALSE = 0x000000;


	// -------
	// EXPORT
	// -------
	@Override public BufferedImage export(BooleanMap input)
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
