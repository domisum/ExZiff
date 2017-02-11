package de.domisum.exziff.shape.exporter;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;

public class RasterShapeImageExporter extends RasterShapeExporter<BufferedImage>
{

	// CONSTANTS
	private static final int COLOR_TRUE = 0xffffff;
	private static final int COLOR_FALSE = 0x000000;


	// -------
	// CONVERSION
	// -------
	@Override
	public BufferedImage export(RasterShape input)
	{
		int[][] pixels = new int[input.getHeight()][input.getWidth()];

		for(int x = 0; x < input.getWidth(); x++)
			for(int y = 0; y < input.getHeight(); y++)
			{
				int color = input.get(x, y) ? COLOR_TRUE : COLOR_FALSE;
				pixels[y][x] = color;
			}

		return ImageUtil.getImageFromPixels(pixels);
	}

}
