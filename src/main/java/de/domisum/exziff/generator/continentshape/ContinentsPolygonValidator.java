package de.domisum.exziff.generator.continentshape;

import de.domisum.lib.auxilium.data.container.math.LineSegment2D;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ContinentsPolygonValidator
{

	// CONSTANTS
	private Polygon2D OUTSIDE_EDGE_POLYGON = new Polygon2D(new Vector2D(0, 0), new Vector2D(1, 0), new Vector2D(1, 1),
			new Vector2D(0, 1), new Vector2D(0, 0), new Vector2D(-1, 2), new Vector2D(2, 2), new Vector2D(2, -1));

	// SETTINGS
	@Getter @Setter private double minPolygonPolygonDistance = 0.08;
	@Getter @Setter private double minPolygonEdgeDistance = 0.08;
	@Getter @Setter private double minPolygonConvexCornerAngleDeg = 35;
	@Getter @Setter private double minPolygonConcaveCornerAngleDeg = 50;
	@Getter @Setter private double minPolygonLineLineDistance = 0.06;

	@Getter @Setter private double lineDistanceBufferDistance = 0.05;


	// VALIDATION
	public boolean validatePolygon(Polygon2D toValidate, Polygon2D toReplace, List<Polygon2D> polygons)
	{
		List<Polygon2D> otherPolygons = new ArrayList<>(polygons);
		otherPolygons.remove(toReplace);

		return validatePolygon(toValidate, otherPolygons);
	}

	private boolean validatePolygon(Polygon2D toValidate, List<Polygon2D> otherPolygons)
	{
		if(doesPolygonSelfIntersect(toValidate))
			return false;

		if(isPolygonTooCloseToOtherPolygons(toValidate, otherPolygons, this.minPolygonPolygonDistance))
			return false;

		// too close to edge
		if(this.OUTSIDE_EDGE_POLYGON.getDistanceTo(toValidate) < this.minPolygonEdgeDistance)
			return false;

		// avoid too pointy angles
		if(doesPolygonHaveTooPointyAngles(toValidate, this.minPolygonConvexCornerAngleDeg, this.minPolygonConcaveCornerAngleDeg))
			return false;

		// avoid very narrow landbridges and very narrow sections of sea
		if(isPolygonTooCloseToSelf(toValidate, this.minPolygonLineLineDistance, this.lineDistanceBufferDistance))
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

}
