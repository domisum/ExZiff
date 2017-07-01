package de.domisum.exziff;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.shape.exporter.ShapeMapImageExporter;
import de.domisum.lib.auxilium.data.container.math.LineSegment2D;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		Random random = new Random(3003);

		for(int i = 0; i < 20; i++)
		{
			long seed = random.nextInt();
			continentGenerationTest(seed+"", seed);
		}
	}

	private static void continentGenerationTest(String fileName, long seed)
	{
		System.out.println(fileName);

		Polygon2D polygon = new Polygon2D(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.25), new Vector2D(.75, .75));

		polygon = randomizePolygon(polygon, seed);

		exportPolygon(polygon, new File("C:\\Users\\domisum\\testChamber/continents/"+fileName+".png"));
	}

	private static Polygon2D randomizePolygon(Polygon2D polygon, long seed)
	{
		Random random = new Random(seed);

		for(int i = 0; i < 50; i++)
		{
			Polygon2D changedPolygon = randomizeSide(polygon, random.nextLong());
			if(changedPolygon != null)
				polygon = changedPolygon;
			else
			{
				//System.out.println(polygon);
				//exportPolygon(polygon, new File("C:\\Users\\domisum\\testChamber/error.png"));
				//System.exit(0);
				i--;
			}
		}

		return polygon;
	}

	private static Polygon2D randomizeSide(Polygon2D polygon, long seed)
	{

		Random random = new Random(seed);

		int side = pickSide(polygon);
		//System.out.println(side);

		Vector2D a = polygon.points.get(side);
		Vector2D b = polygon.points.get(side+1 == polygon.points.size() ? 0 : side+1);

		Vector2D aToB = b.subtract(a);
		Vector2D center = a.add(aToB.multiply(0.5));

		Vector3D perpendicular3D = new Vector3D(aToB.x, aToB.y, 0).crossProduct(new Vector3D(0, 0, 1));
		Vector2D perpendicular = new Vector2D(perpendicular3D.x, perpendicular3D.y).normalize();

		Vector2D possibleOffset = perpendicular.multiply(aToB.length()*0.35);
		Vector2D randomizedOffset = possibleOffset.multiply(RandomUtil.getFromRange(-0.7d, 1, random));

		Vector2D c = center.add(randomizedOffset);

		Vector2D[] points = new Vector2D[polygon.points.size()+1];
		int s = 0;
		for(int t = 0; t < points.length; t++, s++)
		{
			if(t == side+1)
			{
				points[t] = c;
				s--;
			}
			else
				points[t] = polygon.points.get(s);
		}

		Polygon2D newPolygon = new Polygon2D(points);

		//  validate polygon, if illegal return null
		for(LineSegment2D lineSegment : newPolygon.getLines())
		{
			int intersects = 0;
			for(LineSegment2D ls : newPolygon.getLines())
				if(!lineSegment.equals(ls))
					if(lineSegment.intersects(ls))
						intersects++;

			if(intersects != 2)
				return null;
		}

		// check if new point is close to other lines
		List<LineSegment2D> lineSegments = polygon.getLines();
		lineSegments.remove(side);
		for(LineSegment2D lineSegment : lineSegments)
		{
			if(lineSegment.getDistanceTo(c) < 0.01)
			{
				return null;
			}
		}

		return newPolygon;
	}

	private static int pickSide(Polygon2D polygon2D)
	{
		int longestIndex = -1;
		double longestLineLength = 0;

		List<LineSegment2D> lines = polygon2D.getLines();
		for(int i = 0; i < lines.size(); i++)
		{
			double length = lines.get(i).getLength();
			if(length > longestLineLength)
			{
				longestIndex = i;
				longestLineLength = length;
			}
		}

		return longestIndex;
	}

	private static void exportPolygon(Polygon2D polygon2D, File file)
	{
		int renderRes = 1000;
		BooleanMap map = new BooleanMap(renderRes, renderRes);
		renderPolygon(map, polygon2D);

		ShapeMapImageExporter exporter = new ShapeMapImageExporter();
		BufferedImage image = exporter.export(map);
		ImageUtil.writeImage(file, image);
	}

	private static void renderPolygon(BooleanMap booleanMap, Polygon2D polygon2D)
	{
		int steps = booleanMap.getWidth();

		List<LineSegment2D> lines = polygon2D.getLines();
		for(LineSegment2D lineSegment2D : lines)
		{
			Vector2D direction = lineSegment2D.b.subtract(lineSegment2D.a);

			for(int s = 0; s <= steps; s++)
			{
				double current = (double) s/steps;
				Vector2D delta = direction.multiply(current);

				Vector2D currentPosition = lineSegment2D.a.add(delta);


				int pX = (int) Math.round(currentPosition.x*booleanMap.getWidth());
				int pY = (int) Math.round(currentPosition.y*booleanMap.getHeight());

				if(pX < 0 || pX >= booleanMap.getWidth() || pY < 0 || pY >= booleanMap.getHeight())
					continue;

				booleanMap.set(pX, pY, true);
			}
		}
	}


}
