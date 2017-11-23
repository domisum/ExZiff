package de.domisum.exziff.map.converter;

import de.domisum.exziff.map.ShortMap;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class ShortMapToImageConverter implements Converter<ShortMap, BufferedImage>
{

	// CONSTANTS
	private static final List<Color> COLORS = Arrays
			.asList(new Color(240, 163, 255), new Color(0, 117, 220), new Color(153, 63, 0), new Color(76, 0, 92),
					new Color(25, 25, 25), new Color(0, 80, 49), new Color(43, 206, 72), new Color(255, 204, 153),
					new Color(128, 128, 128), new Color(143, 124, 0), new Color(157, 204, 0), new Color(194, 0, 136),
					new Color(0, 51, 128), new Color(255, 164, 5), new Color(75, 117, 0), new Color(255, 0, 16),
					new Color(94, 241, 242), new Color(0, 153, 143), new Color(224, 255, 102), new Color(116, 10, 255),
					new Color(153, 0, 0), new Color(255, 255, 0), new Color(255, 80, 5));

	// CONVERT
	@Override public BufferedImage convert(ShortMap input)
	{
		int[][] pixels = new int[input.getHeight()][input.getWidth()];

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
			{
				Color color = getColorForValue(input.get(x, y));

				pixels[y][x] = color.getRGB(); // TODO is this too performance heavy?
			}

		return ImageUtil.getImageFromPixels(pixels);
	}

	private Color getColorForValue(short value)
	{
		int colorsIndex = value%COLORS.size();
		return COLORS.get(colorsIndex);
	}

}
