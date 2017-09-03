package de.domisum.exziff;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.map.generator.bool.BooleanMapFromImageGenerator;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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


		Random random = new Random(381);
		ShortMap regions = generateRegions(continentShape, random);


		logger.info("Starting export");
		BufferedImage image = export(regions);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//regions.png"), image);
		logger.info("Export done");
	}

	private static ShortMap generateRegions(BooleanMap continentShape, Random random)
	{
		int numberOfPoints = 500;
		List<Vector2D> points = new ArrayList<>();
		while(points.size() < numberOfPoints)
		{
			Vector2D rPoint = new Vector2D(random.nextDouble(), random.nextDouble());
			if(!continentShape.get((int) (rPoint.x*continentShape.getWidth()), (int) (rPoint.y*continentShape.getHeight())))
				continue;

			points.add(rPoint);
		}

		points.sort(Comparator.comparingDouble(p->p.x));
		points.sort(Comparator.comparingDouble(p->p.y));

		ShortMap regions = new ShortMap(continentShape.getWidth(), continentShape.getHeight());
		for(int y = 0; y < regions.getHeight(); y++)
			for(int x = 0; x < regions.getWidth(); x++)
			{
				if(!continentShape.get(x, y))
					continue;

				Vector2D pointHere = new Vector2D(x/(double) continentShape.getWidth(), y/(double) continentShape.getHeight());

				int closestPointIndex = -1;
				double closestPointDistanceSquared = Double.MAX_VALUE;
				for(int i = 0; i < points.size(); i++)
				{
					Vector2D p = points.get(i);
					double distanceSquared = pointHere.distanceToSquared(p);
					if(distanceSquared < closestPointDistanceSquared)
					{
						closestPointIndex = i;
						closestPointDistanceSquared = distanceSquared;
					}
				}

				regions.set(x, y, (short) (closestPointIndex+1));
			}

		return regions;
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
				if(multipleNeighbors(regions, x, y))
					color = -1; // new Color(255, 255, 255).getRGB();

				pixels[y][x] = color;
			}

		return ImageUtil.getImageFromPixels(pixels);
	}

	private static boolean multipleNeighbors(ShortMap shortMap, int x, int y)
	{
		int regionId = shortMap.get(x, y);

		if(!sameAsCheckBounds(shortMap, x, y+1, regionId) || !sameAsCheckBounds(shortMap, x, y-1, regionId) || !sameAsCheckBounds(
				shortMap, x+1, y, regionId) || !sameAsCheckBounds(shortMap, x-1, y, regionId))
			return true;

		return false;
	}

	private static boolean sameAsCheckBounds(ShortMap shortMap, int nX, int nY, int otherValue)
	{
		if(nX < 0 || nX >= shortMap.getWidth() || nY < 0 || nY >= shortMap.getHeight())
			return true;

		int thisValue = shortMap.get(nX, nY);
		if(thisValue == 0)
			return true;

		return thisValue == otherValue;
	}


	private static void worldGeneratorTest()
	{
		File worldDir = new File("C:\\Users\\domisum\\testChamber\\exziff\\testWorld");

		WorldGenerator generator = new WorldGenerator(2048, 1337, worldDir);
		generator.generate();

		System.out.println("done");
	}

}
