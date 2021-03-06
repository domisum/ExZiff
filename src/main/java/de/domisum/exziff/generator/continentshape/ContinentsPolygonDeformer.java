package de.domisum.exziff.generator.continentshape;

import de.domisum.lib.auxilium.data.container.math.LineSegment2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.data.container.math.shape.Polygon2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class ContinentsPolygonDeformer
{

	// SETTINGS
	@Getter
	@Setter
	private double desiredMaxSideLength = 0.06;
	@Getter
	@Setter
	private double maxDivisionBasePointCenterOffset = 0.15;
	@Getter
	@Setter
	private double maxInwardsOffsetMultiplier = 0.5;
	@Getter
	@Setter
	private double maxOutwardsOffsetMultiplier = 0.9;

	// REFERENCES
	private final ContinentsPolygonValidator polygonValidator;


	// GENERATE
	public List<Polygon2D> generate(long seed, List<Polygon2D> polygons)
	{
		return new GenerateMethodObject(new Random(seed), new ArrayList<>(polygons)).deform();
	}


	@RequiredArgsConstructor
	private class GenerateMethodObject
	{

		// INPUT
		private final Random random;
		private final List<Polygon2D> polygons;


		// DEFORM
		public List<Polygon2D> deform()
		{
			PolygonSide longestSide = getLongestSide();
			while(longestSide.getLineSegment().getLength() > desiredMaxSideLength)
			{
				deformPolygonSide(longestSide);

				longestSide = getLongestSide();
			}

			return polygons;
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
			double offsetPointPosAlongSide = RandomUtil.distribute(0.5, maxDivisionBasePointCenterOffset, random);
			Vector2D offsetPoint = a.add(b.subtract(a).multiply(offsetPointPosAlongSide));

			double offsetDistance =
					RandomUtil.getFromRange(-maxInwardsOffsetMultiplier, maxOutwardsOffsetMultiplier, random)*polygonSide
							.getLineSegment()
							.getLength();
			Vector2D newPoint = offsetPoint.add(orthogonalOutward.multiply(offsetDistance));


			// build new polygon
			List<Vector2D> newPolygonPoints = new ArrayList<>();
			for(int i = 0; i <= polygonSide.sideIndex; i++) // points before new point
				newPolygonPoints.add(polygonSide.polygon.getPoints().get(i));

			newPolygonPoints.add(newPoint); // new point itself

			for(int i = polygonSide.sideIndex+1; i < polygonSide.polygon.getPoints().size(); i++) // points after new point
				newPolygonPoints.add(polygonSide.polygon.getPoints().get(i));

			Polygon2D newPolygon = new Polygon2D(newPolygonPoints);


			// validate polygon, if valid replace old poly with new poly
			boolean valid = polygonValidator.isPolygonValid(newPolygon, polygonSide.polygon, polygons);
			if(valid)
			{
				polygons.remove(polygonSide.polygon);
				polygons.add(newPolygon);
			}

			return valid;
		}

		private PolygonSide getLongestSide()
		{
			Polygon2D longestSidePolygon = null;
			int longestSideIndex = -1;
			double longestSideLength = -1;

			for(Polygon2D p : polygons)
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

	}

	// POLYGONSIDE
	@AllArgsConstructor
	private static class PolygonSide
	{

		private final Polygon2D polygon;
		private final int sideIndex;


		// GETTERS
		protected LineSegment2D getLineSegment()
		{
			return polygon.getLines().get(sideIndex);
		}

	}

}
