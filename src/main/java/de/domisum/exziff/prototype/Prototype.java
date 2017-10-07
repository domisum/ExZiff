package de.domisum.exziff.prototype;

import de.domisum.exziff.generator.WorldGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.map.generator.bool.BooleanMapFromImageGenerator;
import de.domisum.exziff.map.transformation.bool.BooleanMapScale;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.math.MathUtil;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Prototype
{

	private final Logger logger = LoggerFactory.getLogger(getClass());
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


	public static void partitioningTest()
	{
		new Prototype().pt();
	}

	public void pt()
	{
		this.logger.info("Starting image loading");
		BooleanMapFromImageGenerator fromImageGenerator = new BooleanMapFromImageGenerator(
				ImageUtil.loadImage("C:\\Users\\domisum\\testChamber\\exziff\\res\\continentShape.png"), 0.5);
		BooleanMap continentShape = fromImageGenerator.generate();
		this.logger.info("Image loading done");


		BooleanMapScale scale = new BooleanMapScale(0.25);
		continentShape = scale.transform(continentShape);
		//continentShape = new BooleanMap(1024, 1024);
		Random random = new Random(3881);
		ShortMap regions = generateRegions(continentShape, random);


		this.logger.info("Starting export");
		BufferedImage image = export(regions);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//regions.png"), image);
		this.logger.info("Export done");
	}


	private static ShortMap generateRegions(BooleanMap continentShape, Random random)
	{
		List<Vector2D> points = generatePoints(continentShape, random);

		LayeredOpenSimplexNoise noise = new OctavedOpenSimplexNoise(7, 0.35, 0.3, 0.2, 0.5, 0);
		List<LayeredOpenSimplexNoise> noises = new ArrayList<>();
		for(int i = 0; i < points.size(); i++)
			noises.add(new LayeredOpenSimplexNoise(noise.getNoiseLayers().getRandomSeedsCopy(random)));


		ShortMap regions = new ShortMap(continentShape.getWidth(), continentShape.getHeight());
		for(int y = 0; y < regions.getHeight(); y++)
		{
			if(y%128 == 0)
				System.out.println(y);

			for(int x = 0; x < regions.getWidth(); x++)
			{
				if(!continentShape.get(x, y))
					continue;

				Vector2D curr = new Vector2D((double) x/regions.getWidth(), (double) y/regions.getHeight());

				double closestDistance = Double.MAX_VALUE;
				int closestPointIndex = -1;
				for(int i = 0; i < points.size(); i++)
				{
					Vector2D p = points.get(i);

					double rawDistance = curr.distanceTo(p);

					double noiseEval = noises.get(i).evaluate(curr.x, curr.y);
					double noiseRemapped = MathUtil.remapLinear(-1, 1, 0, 1, noiseEval);

					double distance = rawDistance*noiseRemapped;

					if(distance < closestDistance)
					{
						closestDistance = distance;
						closestPointIndex = i;
					}
				}

				regions.set(x, y, (short) (closestPointIndex+1));
			}
		}

		for(int i = 0; i < points.size(); i++)
		{
			Vector2D p = points.get(i);

			RegionCoord regionCoord = new RegionCoord((int) (p.x*continentShape.getWidth()),
					(int) (p.y*continentShape.getHeight()), i+1, 0);

			regions.set(regionCoord.x, regionCoord.y, (short) 0);

			//			regions.set(regionCoord.x, regionCoord.y-1, (short) 0);
			//			regions.set(regionCoord.x, regionCoord.y+1, (short) 0);
			//			regions.set(regionCoord.x-1, regionCoord.y, (short) 0);
			//			regions.set(regionCoord.x+1, regionCoord.y, (short) 0);
		}

		return regions;
	}

	private static List<Vector2D> generatePoints(BooleanMap continentShape, Random random)
	{
		int numberOfPoints = 300;


		List<Vector2D> points = new ArrayList<>();
		while(points.size() < numberOfPoints)
		{
			Vector2D rPoint = new Vector2D(random.nextDouble(), random.nextDouble());
			//			if(!continentShape.get((int) (rPoint.x*continentShape.getWidth()), (int) (rPoint.y*continentShape.getHeight())))
			//				continue;

			points.add(rPoint);
		}

		for(int i = 0; i < 5; i++)
			points = balancePoints(points);

		for(int i = 0; i < numberOfPoints/2; i++)
			points.remove(RandomUtil.getFromRange(0, points.size()-1, random));

		points.sort(Comparator.comparingDouble(p->p.y));
		return points;
	}

	private static List<Vector2D> balancePoints(List<Vector2D> points)
	{
		List<Vector2D> newPoints = new ArrayList<>(points);

		for(int i = 0; i < points.size(); i++)
		{
			Vector2D point = points.get(i);

			int numberOfNeighbors = 1;
			Vector2D neighborSum = point;
			for(Vector2D p : points)
				if(p != point && isNeighborPoint(point, p, points))
				{
					numberOfNeighbors++;
					neighborSum = neighborSum.add(point.add(p.subtract(point).divide(2)));
				}

			Vector2D newPoint = neighborSum.divide(numberOfNeighbors);

			if(isEdgePoint(point, points))
				newPoint = point;

			newPoints.set(i, newPoint);
		}

		return newPoints;
	}

	private static boolean isNeighborPoint(Vector2D p1, Vector2D p2, List<Vector2D> points)
	{
		Vector2D p1ToP2 = p2.subtract(p1);
		Vector2D center = p1.add(p1ToP2.divide(2));

		Vector2D orthogonal = p1ToP2.orthogonal().normalize();
		Vector2D centerSideward = orthogonal.multiply(p1ToP2.length()/2);

		Polygon2D polygon = new Polygon2D(p1, center.add(centerSideward), p2, center.subtract(centerSideward));

		for(Vector2D p : points)
			if(p != p1 && p != p2)
				if(polygon.contains(p))
					return false;

		return true;
	}

	private static boolean isEdgePoint(Vector2D p, List<Vector2D> points)
	{
		Polygon2D OUTSIDE_EDGE_POLYGON = new Polygon2D(new Vector2D(0, 0), new Vector2D(1, 0), new Vector2D(1, 1),
				new Vector2D(0, 1), new Vector2D(0, 0), new Vector2D(-1, 2), new Vector2D(2, 2), new Vector2D(2, -1));

		return OUTSIDE_EDGE_POLYGON.getDistanceTo(p) < 0.07;
	}


	private static double distance(double size, List<Vector2D> points, RegionCoord regionCoord)
	{
		Vector2D rRC = new Vector2D(regionCoord.x/size, regionCoord.y/size);
		return rRC.distanceTo(points.get(regionCoord.regionId-1));
	}

	private static void addIfNotRegionSet(BooleanMap continentShape, ShortMap regions, Queue<RegionCoord> queue, int x, int y,
			int regionId, int travelDistance)
	{
		if(!continentShape.get(x, y))
			return;

		if(isOutOfBounds(regions.getWidth(), regions.getHeight(), x, y))
			return;

		if(regions.get(x, y) != 0)
			return;

		queue.add(new RegionCoord(x, y, regionId, travelDistance));
		regions.set(x, y, (short) regionId);
	}


	private static ShortMap randomizeBorders(ShortMap regions, Random random)
	{
		ShortMap newMap = new ShortMap(regions.getWidth(), regions.getHeight());

		for(int y = 0; y < regions.getHeight(); y++)
			for(int x = 0; x < regions.getWidth(); x++)
			{
				short newRegion;

				newRegion = regions.get(x, y);
				if(multipleNeighbors(regions, x, y) && regions.get(x, y) != 0)
					if(RandomUtil.getByChance(0.5, random))
					{
						List<Short> nonselfNeighbors = getNonselfNeighbors(regions, x, y);
						newRegion = RandomUtil.getElement(nonselfNeighbors, random);
					}

				newMap.set(x, y, newRegion);
			}

		for(int y = 0; y < regions.getHeight(); y++)
			for(int x = 0; x < regions.getWidth(); x++)
			{
				short self = newMap.get(x, y);
				if(self == 0)
					continue;

				if(!multipleNeighbors(newMap, x, y))
					continue;

				List<Short> neighbors = getNonselfNeighbors(newMap, x, y);

				if(neighbors.contains(self))
					continue;

				newMap.set(x, y, neighbors.get(0));
			}

		return newMap;
	}

	private static List<Short> getNonselfNeighbors(ShortMap regions, int x, int y)
	{
		List<Short> nsn = new ArrayList<>();

		short self = regions.get(x, y);

		addIfNotSame(regions, nsn, self, x+1, y);
		addIfNotSame(regions, nsn, self, x-1, y);
		addIfNotSame(regions, nsn, self, x, y+1);
		addIfNotSame(regions, nsn, self, x, y-1);

		return nsn;
	}

	private static void addIfNotSame(ShortMap regions, List<Short> nsn, short self, int nX, int nY)
	{
		short v = regions.get(nX, nY);
		if(v == 0)
			return;

		nsn.add(v);
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
				//				if(multipleNeighbors(regions, x, y))
				//					color = -1; // new Color(255, 255, 255).getRGB();

				pixels[y][x] = color;
			}

		return ImageUtil.getImageFromPixels(pixels);
	}

	private static boolean multipleNeighbors(ShortMap shortMap, int x, int y)
	{
		int regionId = shortMap.get(x, y);

		if(otherThanCheckBounds(shortMap, x, y+1, regionId) || otherThanCheckBounds(shortMap, x, y-1, regionId)
				|| otherThanCheckBounds(shortMap, x+1, y, regionId) || otherThanCheckBounds(shortMap, x-1, y, regionId))
			return true;

		return false;
	}

	private static boolean otherThanCheckBounds(ShortMap shortMap, int nX, int nY, int otherValue)
	{
		if(isOutOfBounds(shortMap.getWidth(), shortMap.getHeight(), nX, nY))
			return false;

		int thisValue = shortMap.get(nX, nY);
		if(thisValue == 0)
			return false;

		return thisValue != otherValue;
	}

	private static boolean isOutOfBounds(int width, int height, int nX, int nY)
	{
		return nX < 0 || nX >= width || nY < 0 || nY >= height;
	}


	private static void worldGeneratorTest()
	{
		File worldDir = new File("C:\\Users\\domisum\\testChamber\\exziff\\testWorld");

		WorldGenerator generator = new WorldGenerator(2048, 1337, worldDir);
		generator.generate();

		System.out.println("done");
	}

}
