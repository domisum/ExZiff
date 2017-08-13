package de.domisum.exziff.shape;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.exporter.bool.BooleanMapImageExporter;
import de.domisum.exziff.map.generator.bool.BooleanMapGenerator;
import de.domisum.exziff.map.generator.bool.BooleanMapPolygonGenerator;
import de.domisum.lib.auxilium.data.container.math.LineSegment2D;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class ContinentsShapeGenerator extends BooleanMapGenerator
{

	// CONSTANTS
	private Polygon2D OUTSIDE_EDGE_POLYGON = new Polygon2D(new Vector2D(0, 0), new Vector2D(1, 0), new Vector2D(1, 1),
			new Vector2D(0, 1), new Vector2D(0, 0), new Vector2D(-1, 2), new Vector2D(2, 2), new Vector2D(2, -1));

	// settings
	private int size;
	private long seed;

	@Getter @Setter private int desiredPolygonsBase = 3;
	@Getter @Setter private int desiredPolygonsMaxDifference = 1;

	@Getter @Setter private double desiredMaxSideLength = 0.06;
	@Getter @Setter private double maxDivisionBasePointCenterOffset = 0.15;
	@Getter @Setter private double maxInwardsOffsetMultiplier = 0.5;
	@Getter @Setter private double maxOutwardsOffsetMultiplier = 0.9;

	@Getter @Setter private double minPolygonPolygonDistance = 0.08;
	@Getter @Setter private double minPolygonEdgeDistance = 0.08;
	@Getter @Setter private double minPolygonConvexCornerAngleDeg = 35;
	@Getter @Setter private double minPolygonConcaveCornerAngleDeg = 50;
	@Getter @Setter private double minPolygonLineLineDistance = 0.06;

	@Getter @Setter private int downscalingFactor = 1;

	// temp
	private Random random;
	private List<Polygon2D> polygons = new ArrayList<>();


	// INIT
	public ContinentsShapeGenerator(int size, long seed)
	{
		this.size = size;
		this.seed = seed;
	}


	// GENERATE
	@Override public BooleanMap generate()
	{
		this.random = new Random(this.seed);
		System.out.println(this.seed);

		generateBasePolygons();
		deformPolygons();

		BooleanMapPolygonGenerator generator = new BooleanMapPolygonGenerator(this.size/this.downscalingFactor, this.polygons);
		return generator.generate();
	}


	// BASE POLYGONS
	private void generateBasePolygons()
	{
		int numberOfPolygonsToGenerate = RandomUtil
				.distribute(this.desiredPolygonsBase, this.desiredPolygonsMaxDifference, this.random);

		// generate small base polygons
		while(this.polygons.size() < numberOfPolygonsToGenerate)
		{
			Polygon2D newPolygon = generateMiniBasePolygon();

			boolean valid = validateReplacementPolygon(newPolygon, null);
			if(valid)
				this.polygons.add(newPolygon);
		}

		// modify the base polygons
		for(int i = 0; i < 1000; i++)
		{
			Polygon2D toModify = RandomUtil.getElement(this.polygons, this.random);

			Polygon2D modified = modifyBasePolygon(toModify);

			boolean valid = validateReplacementPolygon(modified, toModify);
			if(valid)
			{
				this.polygons.remove(toModify);
				this.polygons.add(modified);
			}
		}
	}

	private Polygon2D generateMiniBasePolygon()
	{
		double radius = 0.05;

		double centerX = RandomUtil.getFromRange(this.minPolygonEdgeDistance, 1-this.minPolygonEdgeDistance, this.random);
		double centerY = RandomUtil.getFromRange(this.minPolygonEdgeDistance, 1-this.minPolygonEdgeDistance, this.random);
		Vector2D center = new Vector2D(centerX, centerY);

		List<Vector2D> points = new Vector<>();
		for(int i = 0; i < 3; i++)
		{
			Vector2D randomOffset = RandomUtil.getUnitVector2D(this.random).multiply(radius);

			Vector2D point = center.add(randomOffset);
			points.add(point);
		}

		return new Polygon2D(points);
	}

	private Polygon2D modifyBasePolygon(Polygon2D toModify)
	{
		if(RandomUtil.nextDouble(this.random) < 0.3)
			return modifyBasePolygonMove(toModify);

		return modifyBasePolygonDeform(toModify);
	}

	private Polygon2D modifyBasePolygonDeform(Polygon2D toModify)
	{
		Vector2D center = toModify.getPointCenter();
		Vector2D point = RandomUtil.getElement(toModify.points, this.random);

		Vector2D centerToPoint = point.subtract(center);
		Vector2D ctpDirection = centerToPoint.normalize();
		Vector2D ctpOrthogonal = center.orthogonal().normalize();

		Vector2D pointDeltaForward = ctpDirection.multiply(RandomUtil.distribute(0.05, 0.1, this.random));
		Vector2D pointDeltaSideward = ctpOrthogonal.multiply(RandomUtil.distribute(0.05, 0.1, this.random));
		Vector2D pointDelta = pointDeltaForward.add(pointDeltaSideward);

		Vector2D newPoint = point.add(pointDelta);
		List<Vector2D> newPoints = new ArrayList<>(toModify.points);
		newPoints.replaceAll((p)->Objects.equals(p, point) ? newPoint : p); // replace the old point with the new one

		return new Polygon2D(newPoints);
	}

	private Polygon2D modifyBasePolygonMove(Polygon2D toModify)
	{
		Vector2D movementDirection = RandomUtil.getUnitVector2D(this.random);

		Vector2D movement = movementDirection.multiply(RandomUtil.distribute(0.05, 0.1, this.random));
		return toModify.move(movement);
	}


	// DEFORMATION
	private void deformPolygons()
	{
		PolygonSide longestSide = getLongestSide();

		while(longestSide.getLineSegment().getLength() > this.desiredMaxSideLength)
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

		int deformTries = 0;
		boolean deformSuccess = false;
		while(!deformSuccess)
		{
			deformSuccess = tryDeformPolygonSide(polygonSide, orthogonalOutward);
			deformTries++;

			// debugging purpose, remove later
			if(deformTries > 1000)
				exportFail(polygonSide);
		}
	}

	private boolean tryDeformPolygonSide(PolygonSide polygonSide, Vector2D orthogonalOutward)
	{
		Vector2D a = polygonSide.getLineSegment().a;
		Vector2D b = polygonSide.getLineSegment().b;

		// determine random points
		double offsetPointPosAlongSide = RandomUtil.distribute(0.5, this.maxDivisionBasePointCenterOffset, this.random);
		Vector2D offsetPoint = a.add(b.subtract(a).multiply(offsetPointPosAlongSide));

		double offsetDistance =
				RandomUtil.getFromRange(-this.maxInwardsOffsetMultiplier, this.maxOutwardsOffsetMultiplier, this.random)
						*polygonSide.getLineSegment().getLength();
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


	// VALIDATION
	private boolean validateReplacementPolygon(Polygon2D newPolygon, Polygon2D toReplace)
	{
		if(doesPolygonSelfIntersect(newPolygon))
			return false;

		List<Polygon2D> otherPolygons = new ArrayList<>(this.polygons);
		otherPolygons.remove(toReplace);
		if(isPolygonTooCloseToOtherPolygons(newPolygon, otherPolygons, this.minPolygonPolygonDistance))
			return false;

		// too close to edge
		if(this.OUTSIDE_EDGE_POLYGON.getDistanceTo(newPolygon) < this.minPolygonEdgeDistance)
			return false;

		// avoid too pointy angles
		if(doesPolygonHaveTooPointyAngles(newPolygon, this.minPolygonConvexCornerAngleDeg, this.minPolygonConcaveCornerAngleDeg))
			return false;

		// avoid very narrow landbridges and very narrow sections of sea
		if(isPolygonTooCloseToSelf(newPolygon, this.minPolygonLineLineDistance, this.desiredMaxSideLength))
			return false;

		return true;
	}

	private static boolean doesPolygonSelfIntersect(Polygon2D polygon)
	{
		for(LineSegment2D lineSegment : polygon.getLines())
		{
			int intersects = 0;
			for(LineSegment2D ls : polygon.getLines())
				if(!lineSegment.equals(ls) && lineSegment.intersects(ls))
					intersects++;

			if(intersects != 2)
				return true;
		}

		return false;
	}

	private static boolean isPolygonTooCloseToOtherPolygons(Polygon2D polygon, List<Polygon2D> others, double minDistance)
	{
		for(Polygon2D p : others)
			if(polygon.getDistanceTo(p) < minDistance)
				return true;

		return false;
	}

	private static boolean doesPolygonHaveTooPointyAngles(Polygon2D polygon, double minConvexAngleDeg, double minConcaveAngleDeg)
	{
		// initialize with last linesegment, since it is first.before
		for(Polygon2D.PolygonCorner pc : polygon.getCorners())
		{
			double minAngleDeg =
					pc.orientation == Polygon2D.PolygonCornerOrientation.CONVEX ? minConvexAngleDeg : minConcaveAngleDeg;

			if(pc.angleDegAbs < minAngleDeg)
				return true;
		}

		return false;
	}

	private static boolean isPolygonTooCloseToSelf(Polygon2D polygon, double minLineLineDistance, double lineDistanceBuffer)
	{
		int size = polygon.points.size();

		for(int li1 = 0; li1 < size; li1++)
			for(int li2 = 0; li2 < size; li2++)
			{
				if(li1 == li2)
					continue;

				// if this applies, we already went through the line combination before, with indexes switched
				if(li2 < li1)
					continue;

				double throughLinesDistance = getLineLineDistanceThroughLines(polygon, li1, li2);
				if(throughLinesDistance == 0) // lines are direct neighbors
					continue;

				double directDistance = polygon.getLines().get(li1).getDistanceTo(polygon.getLines().get(li2));

				// are lines far enough apart to check absolute distance
				if(throughLinesDistance/(directDistance+lineDistanceBuffer) < 2)
					continue;

				if(directDistance < minLineLineDistance)
				{
					//System.out.println(li1+" "+li2 + " tl: " + throughLinesDistance + " direct: " + directDistance);
					return true;
				}
			}

		return false;
	}

	private static double getLineLineDistanceThroughLines(Polygon2D polygon, int li1, int li2)
	{
		int minIndex = Math.min(li1, li2);
		int maxIndex = Math.max(li1, li2);

		double betweenDistance = 0;
		double overEdgeDistance = 0;
		for(int i = 0; i < polygon.getLines().size(); i++)
		{
			double lineLength = polygon.getLines().get(i).getLength();

			if(i > minIndex && i < maxIndex)
				betweenDistance += lineLength;

			if(i < minIndex || i > maxIndex)
				overEdgeDistance += lineLength;
		}

		return Math.min(betweenDistance, overEdgeDistance);
	}


	// POLYGONSIDE
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


	private static void exportFail(PolygonSide polygonSide)
	{
		System.out.println("fail");
		System.out.println(polygonSide.sideIndex);

		List<Polygon2D> polygons = new ArrayList<>();
		polygons.add(polygonSide.polygon);
		BooleanMapPolygonGenerator generator = new BooleanMapPolygonGenerator(1024, polygons);
		BooleanMap booleanMap = generator.generate();

		LineSegment2D ls = polygonSide.getLineSegment();

		double d = 0.001;
		for(int x = 0; x < 1024; x++)
			for(int y = 0; y < 1024; y++)
			{
				double rX = x/1024d;
				double rY = y/1024d;
				if(Math.abs(rX-ls.a.x) < d || Math.abs(rX-ls.b.x) < d || Math.abs(rY-ls.a.y) < d || Math.abs(rY-ls.b.y) < d)
					booleanMap.set(x, y, true);
			}

		BooleanMapImageExporter exporter = new BooleanMapImageExporter();
		BufferedImage image = exporter.export(booleanMap);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\continentFail.png"), image);


		System.exit(0);
	}

}
