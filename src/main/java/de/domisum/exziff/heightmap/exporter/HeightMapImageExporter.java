package de.domisum.exziff.heightmap.exporter;

import de.domisum.exziff.heightmap.HeightMap;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;

public class HeightMapImageExporter extends HeightMapExporter<BufferedImage>
{

	// EXPORT
	@Override public BufferedImage export(HeightMap input)
	{
		int[][] pixels = new int[input.getHeight()][input.getWidth()];

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
			{
				double brightness = input.get(x, y);
				int brightnessInt = (int) Math.floor(brightness*256);
				int color = (brightnessInt)|(brightnessInt<<8)|(brightnessInt<<16);

				pixels[y][x] = color;
			}

		return ImageUtil.getImageFromPixels(pixels);
	}

}
