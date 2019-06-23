package de.domisum.exziff.generator.bedrockpartition;

import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.exziff.generator.util.PoissonDiskPointGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
public class NearestPointPartitionGenerator implements RandomizedGeneratorOneInput<BooleanMap, ShortMap>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// REFERENCES
	private final PoissonDiskPointGenerator pointGenerator = new PoissonDiskPointGenerator();


	// GENERATE
	@Override
	public ShortMap generate(long seed, BooleanMap continentShape)
	{
		return new GenerateMethodObject(new Random(seed), continentShape).generate();
	}

	@RequiredArgsConstructor
	private class GenerateMethodObject
	{

		// INPUT
		private final Random random;
		private final BooleanMap continentShape;

		// TEMP
		private final Set<RegionCenterPoint> regionCenterPoints = new HashSet<>();


		// GENERATE
		public ShortMap generate()
		{
			generateRegionCenterPoints();

			ShortMap closestPointMap = generateClosestPointMap();
			return closestPointMap;
		}

		private void generateRegionCenterPoints()
		{
			logger.info("Generating center points...");

			Set<Vector2D> points = pointGenerator.generate(random.nextLong(), 0.05, 0.0);
			short regionIdCounter = 1;
			for(Vector2D p : points)
			{
				regionCenterPoints.add(new RegionCenterPoint(regionIdCounter, p));
				regionIdCounter++;
			}
		}

		private ShortMap generateClosestPointMap()
		{
			logger.info("Genereating closest point map...");

			ShortMap closestPointMap = new ShortMap(continentShape.getWidth(), continentShape.getHeight());
			for(int x = 0; x < closestPointMap.getWidth(); x++)
				for(int y = 0; y < closestPointMap.getHeight(); y++)
				{
					if(!continentShape.get(x, y))
						continue;

					double rX = x/(double) closestPointMap.getWidth();
					double rY = y/(double) closestPointMap.getHeight();
					RegionCenterPoint closestRegionCenterPoint = getClosestRegionCenterPoint(rX, rY);

					closestPointMap.set(x, y, closestRegionCenterPoint.regionId);
				}

			return closestPointMap;
		}

		private RegionCenterPoint getClosestRegionCenterPoint(double rX, double rY)
		{
			Vector2D fromPoint = new Vector2D(rX, rY);

			RegionCenterPoint closetRegionCenterPoint = null;
			double closestRegionCenterPointDistanceSquared = Double.MAX_VALUE;
			for(RegionCenterPoint rcp : regionCenterPoints)
			{
				double distance = fromPoint.distanceToSquared(rcp.point);
				if(distance < closestRegionCenterPointDistanceSquared)
				{
					closetRegionCenterPoint = rcp;
					closestRegionCenterPointDistanceSquared = distance;
				}
			}

			return closetRegionCenterPoint;
		}

	}


	@AllArgsConstructor
	private static class RegionCenterPoint
	{

		private final short regionId;
		private final Vector2D point;

	}

}

