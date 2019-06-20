package de.domisum.exziff.generator.continentshape;

import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.data.container.math.shape.Polygon2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Generates simple polygons as a starting point for ContinentsShapeGenerator.
 */
@RequiredArgsConstructor
public class ContinentsBasePolygonGenerator implements RandomizedGeneratorOneInput<Integer, List<Polygon2D>>
{

	// REFERENCES
	private final ContinentsPolygonValidator polygonValidator;


	// BASE POLYGONS
	@Override
	public List<Polygon2D> generate(long seed, Integer desiredNumberOfPolygons)
	{
		return new GenerateMethodObject(new Random(seed), desiredNumberOfPolygons).generate();
	}


	@RequiredArgsConstructor
	private class GenerateMethodObject // TODO simplify methods
	{

		// INPUT
		private final Random random;
		private final int desiredNumberOfPolygons;

		// OUTPUT
		private final List<Polygon2D> polygons = new ArrayList<>();


		public List<Polygon2D> generate()
		{
			// generate small base polygons
			while(polygons.size() < desiredNumberOfPolygons)
			{
				Polygon2D newPolygon = generateMiniBasePolygon();

				if(polygonValidator.isPolygonValid(newPolygon, null, polygons))
					polygons.add(newPolygon);
			}

			// modify the base polygons
			for(int i = 0; i < 1000; i++)
			{
				Polygon2D toModify = RandomUtil.getElement(polygons, random);

				Polygon2D modified = modifyBasePolygon(toModify);

				boolean valid = polygonValidator.isPolygonValid(modified, toModify, polygons);
				if(valid)
				{
					polygons.remove(toModify);
					polygons.add(modified);
				}
			}

			return polygons;
		}

		private Polygon2D generateMiniBasePolygon()
		{
			double radius = 0.05;

			double edgeDistance = polygonValidator.getMinToEdgeDistance();
			double centerX = RandomUtil.getFromRange(edgeDistance, 1-edgeDistance, random);
			double centerY = RandomUtil.getFromRange(edgeDistance, 1-edgeDistance, random);
			Vector2D center = new Vector2D(centerX, centerY);

			List<Vector2D> points = new ArrayList<>();
			for(int i = 0; i < 3; i++)
			{
				Vector2D randomOffset = RandomUtil.getUnitVector2D(random).multiply(radius);

				Vector2D point = center.add(randomOffset);
				points.add(point);
			}

			return new Polygon2D(points);
		}

		private Polygon2D modifyBasePolygon(Polygon2D toModify)
		{
			if(RandomUtil.nextDouble(random) < 0.3)
				return modifyBasePolygonMove(toModify);

			return modifyBasePolygonDeform(toModify);
		}

		private Polygon2D modifyBasePolygonDeform(Polygon2D toModify)
		{
			Vector2D center = toModify.getPointCenter();
			Vector2D point = RandomUtil.getElement(toModify.getPoints(), random);

			Vector2D centerToPoint = point.subtract(center);
			Vector2D ctpDirection = centerToPoint.normalize();
			Vector2D ctpOrthogonal = center.orthogonal().normalize();

			Vector2D pointDeltaForward = ctpDirection.multiply(RandomUtil.distribute(0.05, 0.1, random));
			Vector2D pointDeltaSideward = ctpOrthogonal.multiply(RandomUtil.distribute(0.05, 0.1, random));
			Vector2D pointDelta = pointDeltaForward.add(pointDeltaSideward);

			Vector2D newPoint = point.add(pointDelta);
			List<Vector2D> newPoints = new ArrayList<>(toModify.getPoints());
			newPoints.replaceAll(p->Objects.equals(p, point) ? newPoint : p); // replace the old point with the new one

			return new Polygon2D(newPoints);
		}

		private Polygon2D modifyBasePolygonMove(Polygon2D toModify)
		{
			Vector2D movementDirection = RandomUtil.getUnitVector2D(random);

			Vector2D movement = movementDirection.multiply(RandomUtil.distribute(0.05, 0.1, random));
			return toModify.move(movement);
		}

	}
}
