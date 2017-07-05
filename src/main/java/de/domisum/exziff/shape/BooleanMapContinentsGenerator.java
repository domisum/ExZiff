package de.domisum.exziff.shape;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.bool.BooleanMapGenerator;
import de.domisum.lib.auxilium.data.container.math.LineSegment2D;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BooleanMapContinentsGenerator extends BooleanMapGenerator
{

	// CONSTANTS
	private Polygon2D OUTSIDE_EDGE_POLYGON = new Polygon2D(new Vector2D(0, 0), new Vector2D(1, 0), new Vector2D(1, 1),
			new Vector2D(0, 1), new Vector2D(0, 0), new Vector2D(-1, 2), new Vector2D(2, 2), new Vector2D(2, -1));

	// settings
	private int size;
	private long seed;

	@Getter @Setter private int downscalingFactor = 1;

	// temp
	private Random random;
	private List<Polygon2D> polygons = new ArrayList<>();

	// output
	private BooleanMap output;


	// INIT
	public BooleanMapContinentsGenerator(int size, long seed)
	{
		this.size = size;
		this.seed = seed;
	}


	// GENERATE
	@Override public BooleanMap generate()
	{
		this.random = new Random(this.seed);

		generateBasePolygons();
		deformPolygons();
		convertToBooleanMap();

		return this.output;
	}

	private void generateBasePolygons()
	{
		this.polygons.add(new Polygon2D(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.25), new Vector2D(.75, .75)));
		this.polygons.add(new Polygon2D(new Vector2D(0.1, 0.9), new Vector2D(.1, .4), new Vector2D(0.5, 0.9)));

		// TODO make sure the initially generated polygons are valid
	}


	// DEFORMATION
	private void deformPolygons()
	{
		PolygonSide longestSide = getLongestSide();

		while(longestSide.getLineSegment().getLength() > 0.05)
		//for(int i = 0; i < 200; i++)
		{
			deformPolygonSide(longestSide);

			longestSide = getLongestSide();
		}
	}

	private void deformPolygonSide(PolygonSide polygonSide)
	{
		Vector2D a = polygonSide.getLineSegment().a;
		Vector2D b = polygonSide.getLineSegment().b;

		Vector2D aToB = b.subtract(a);
		Vector2D sideCenter = a.add(aToB.multiply(0.5));

		Vector2D orthogonal = aToB.orthogonal();

		// fix direction of orthogonal vector to point outwards
		Vector2D orthogonalOutward;
		if(!polygonSide.polygon.contains(sideCenter.add(orthogonal.multiply(0.0001))))
			orthogonalOutward = orthogonal;
		else
			orthogonalOutward = orthogonal.invert();


		boolean deformSuccess = false;
		while(!deformSuccess)
			deformSuccess = tryDeformPolygonSide(polygonSide, orthogonalOutward);
	}

	private boolean tryDeformPolygonSide(PolygonSide polygonSide, Vector2D orthogonalOutward)
	{
		Vector2D a = polygonSide.getLineSegment().a;
		Vector2D b = polygonSide.getLineSegment().b;

		// determine random points
		double offsetPointPosAlongSide = RandomUtil.getFromRange(0.4, 1-0.4, this.random);
		Vector2D offsetPoint = a.add(b.subtract(a).multiply(offsetPointPosAlongSide));

		double offsetDistance = RandomUtil.getFromRange(-0.5, 1, this.random)*polygonSide.getLineSegment().getLength();
		Vector2D newPoint = offsetPoint.add(orthogonalOutward.multiply(offsetDistance));


		// build new polygon
		List<Vector2D> newPolygonPoints = new ArrayList<>();
		for(int i = 0; i <= polygonSide.sideIndex; i++) // points before new point
			newPolygonPoints.add(polygonSide.polygon.points.get(i));

		newPolygonPoints.add(newPoint); // new point itself

		for(int i = polygonSide.sideIndex+1; i < polygonSide.polygon.points.size(); i++) // points after new point
			newPolygonPoints.add(polygonSide.polygon.points.get(i));

		Polygon2D newPolygon = new Polygon2D(newPolygonPoints);


		// validate polygon, if valid replace old poly with new poly
		boolean valid = validateReplacementPolygon(newPolygon, polygonSide.polygon);

		if(valid)
		{
			this.polygons.remove(polygonSide.polygon);
			this.polygons.add(newPolygon);
		}

		return valid;
	}


	private boolean validateReplacementPolygon(Polygon2D newPolygon, Polygon2D toReplace)
	{
		// polygon self intersection
		for(LineSegment2D lineSegment : newPolygon.getLines())
		{
			int intersects = 0;
			for(LineSegment2D ls : newPolygon.getLines())
				if(!lineSegment.equals(ls))
					if(lineSegment.intersects(ls))
						intersects++;

			if(intersects != 2)
				return false;
		}

		// too close to other polygons
		for(Polygon2D p : this.polygons)
		{
			if(p == toReplace)
				continue;

			if(newPolygon.getDistanceTo(p) < 0.05)
				return false;
		}

		// too close to edge
		if(this.OUTSIDE_EDGE_POLYGON.getDistanceTo(newPolygon) < 0.05)
			return false;

		// avoid too pointy angles
		LineSegment2D lineSegmentBefore = newPolygon.getLines().get(newPolygon.getLines().size()-1);
		for(LineSegment2D ls : newPolygon.getLines())
		{
			double angleRad = lineSegmentBefore.getDirection().getAngleTo(ls.getDirection().invert());

			if(Math.toDegrees(angleRad) < 30)
				return false;

			lineSegmentBefore = ls;
		}

		return true;
	}

	private PolygonSide getLongestSide()
	{
		Polygon2D longestSidePolygon = null;
		int longestSideIndex = -1;
		double longestSideLength = -1;

		for(Polygon2D p : this.polygons)
			for(int i = 0; i < p.getLines().size(); i++)
			{
				LineSegment2D side = p.getLines().get(i);
				double sideLength = side.getLength();

				if(sideLength > longestSideLength)
				{
					longestSidePolygon = p;
					longestSideIndex = i;
					longestSideLength = sideLength;
				}
			}

		return new PolygonSide(longestSidePolygon, longestSideIndex);
	}


	// conversion
	private void convertToBooleanMap()
	{
		int downscaledSize = this.size/this.downscalingFactor;
		this.output = new BooleanMap(downscaledSize, downscaledSize);

		for(int y = 0; y < downscaledSize; y++)
			for(int x = 0; x < downscaledSize; x++)
			{
				double rX = (double) x/downscaledSize;
				double rY = (double) y/downscaledSize;
				Vector2D relPos = new Vector2D(rX, rY);

				for(Polygon2D p : this.polygons)
					if(p.contains(relPos))
					{
						this.output.set(x, y, true);
						break;
					}
			}
	}


	@AllArgsConstructor
	private class PolygonSide
	{

		private Polygon2D polygon;
		private int sideIndex;


		// GETTERS
		public LineSegment2D getLineSegment()
		{
			return this.polygon.getLines().get(this.sideIndex);
		}

	}

}
