package de.domisum.exziff;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.map.generator.bool.BooleanMapFromImageGenerator;
import de.domisum.lib.auxilium.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TestLauncher
{

	private static Logger logger = LoggerFactory.getLogger(TestLauncher.class);
	private static int[][] colorsRGB = new int[][] {{240, 163, 255}, {0, 117, 220}, {153, 63, 0}, {76, 0, 92}, {25, 25, 25},
			{0, 80, 49}, {43, 206, 72}, {255, 204, 153}, {128, 128, 128}, {143, 124, 0}, {157, 204, 0}, {194, 0, 136},
			{0, 51, 128}, {255, 164, 5}, {75, 117, 0}, {255, 0, 16}, {94, 241, 242}, {0, 153, 143}, {224, 255, 102},
			{116, 10, 255}, {153, 0, 0}, {255, 255, 0}, {255, 80, 5}};

	private static int[] colorsInt = new int[colorsRGB.length];

	static
	{
		for(int i = 0; i < colorsInt.length; i++)
		{
			int[] rgb = colorsRGB[i];
			colorsInt[i] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
		}
	}


	public static void main(String[] args)
	{
		partitioningTest();
		//		worldGeneratorTest();
	}

	private static void partitioningTest()
	{
		logger.info("Starting image loading");
		BooleanMapFromImageGenerator fromImageGenerator = new BooleanMapFromImageGenerator(
				ImageUtil.loadImage("C:\\Users\\domisum\\testChamber\\exziff\\res\\continentShape.png"), 0.5);
		BooleanMap continentShape = fromImageGenerator.generate();
		logger.info("Image loading done");


		ShortMap regions = new ShortMap(continentShape.getWidth(), continentShape.getHeight());
		for(int y = 0; y < regions.getHeight(); y++)
			for(int x = 0; x < regions.getWidth(); x++)
			{
				if(!continentShape.get(x, y))
					continue;

				int size = 64*4;
				int redX = x/size;
				int redY = y/size;
				regions.set(x, y, (short) ((redY*(regions.getWidth()/size))+redX+1));
			}


		logger.info("Starting export");
		BufferedImage image = export(regions);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//regions.png"), image);
		logger.info("Export done");
	}

	private static BufferedImage export(ShortMap regions)
	{
		int[][] pixels = new int[regions.getHeight()][regions.getWidth()];

		for(int y = 0; y < regions.getHeight(); y++)
			for(int x = 0; x < regions.getWidth(); x++)
			{
				int regionId = regions.get(x, y);
				if(regionId == 0)
					continue;

				int color = colorsInt[regionId%colorsInt.length];
				pixels[y][x] = color;
			}

		return ImageUtil.getImageFromPixels(pixels);
	}


	private static void worldGeneratorTest()
	{
		File worldDir = new File("C:\\Users\\domisum\\testChamber\\exziff\\testWorld");

		WorldGenerator generator = new WorldGenerator(2048, 1337, worldDir);
		generator.generate();

		System.out.println("done");
	}

}
