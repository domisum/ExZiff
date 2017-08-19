package de.domisum.exziff.shape;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.bool.BooleanMapGenerator;
import de.domisum.exziff.map.generator.bool.BooleanMapPolygonGenerator;
import de.domisum.exziff.shape.continent.ContinentShapePolygonValidator;
import de.domisum.lib.auxilium.data.container.math.LineSegment2D;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class ContinentsShapeGenerator extends BooleanMapGenerator
{

	// settings
	private int size;
	private long seed;

	@Getter @Setter private int desiredPolygonsBase = 3;
	@Getter @Setter private int desiredPolygonsMaxDifference = 1;

	@Getter @Setter private double desiredMaxSideLength = 0.06;
	@Getter @Setter private double maxDivisionBasePointCenterOffset = 0.15;
	@Getter @Setter private double maxInwardsOffsetMultiplier = 0.5;
	@Getter @Setter private double maxOutwardsOffsetMultiplier = 0.9;

	@Getter @Setter private int downscalingFactor = 1;

	// references
	private ContinentShapePolygonValidator polygonValidator = new ContinentShapePolygonValidator();

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

			boolean valid = this.polygonValidator.validatePolygon(newPolygon, null, this.polygons);
			if(valid)
				this.polygons.add(newPolygon);
		}

		// modify the base polygons
		for(int i = 0; i < 1000; i++)
		{
			Polygon2D toModify = RandomUtil.getElement(this.polygons, this.random);

			Polygon2D modified = modifyBasePolygon(toModify);

			boolean valid = this.polygonValidator.validatePolygon(modified, toModify, this.polygons);
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

		double edgeDistance = this.polygonValidator.getMinPolygonEdgeDistance();
		double centerX = RandomUtil.getFromRange(edgeDistance, 1-edgeDistance, this.random);
		double centerY = RandomUtil.getFromRange(edgeDistance, 1-edgeDistance, this.random);
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

		boolean deformSuccess = false;
		while(!deformSuccess)
			deformSuccess = tryDeformPolygonSide(polygonSide, orthogonalOutward);
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
		boolean valid = this.polygonValidator.validatePolygon(newPolygon, polygonSide.polygon, this.polygons);
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

}
