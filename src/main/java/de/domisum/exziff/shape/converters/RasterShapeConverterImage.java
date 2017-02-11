package de.domisum.exziff.shape.converters;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;

public class RasterShapeConverterImage extends RasterShapeConverter<BufferedImage>
{

	// CONSTANTS
	private static final int COLOR_TRUE = 0xffffff;
	private static final int COLOR_FALSE = 0x000000;


	// -------
	// INITIALIZATION
	// -------
	public RasterShapeConverterImage(RasterShape rasterShape)
	{
		super(rasterShape);
	}


	// -------
	// CONVERSION
	// -------
	@Override
	public void convert()
	{
		int[][] pixels = new int[this.rasterShape.getHeight()][this.rasterShape.getWidth()];

		for(int x = 0; x < this.rasterShape.getWidth(); x++)
			for(int y = 0; y < this.rasterShape.getHeight(); y++)
			{
				int color = this.rasterShape.get(x, y) ? COLOR_TRUE : COLOR_FALSE;
				pixels[y][x] = color;
			}

		this.output = ImageUtil.getImageFromPixels(pixels);
	}

}
