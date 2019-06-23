package de.domisum.exziff.generator.continentshape;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.bool.BooleanMapFromPolygons;
import de.domisum.exziff.map.transformer.bool.BooleanMapScale;
import de.domisum.lib.auxilium.data.container.math.shape.Polygon2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * Generates random continent shapes from a given seed.
 */
@RequiredArgsConstructor
public class ContinentsShapeGeneratorUsingDeformedPolygons implements ContinentsShapeGenerator
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	private static final int POLYGON_MAP_DOWNSCALING_FACTOR = 4;

	// SETTINGS
	@Getter
	@Setter
	private int downscalingFactor = 1;

	// REFERENCES
	private final ContinentsBasePolygonGenerator continentsBasePolygonGenerator = new ContinentsBasePolygonGenerator(new ContinentsPolygonValidator());
	private final ContinentsPolygonDeformer continentsPolygonDeformer = new ContinentsPolygonDeformer(new ContinentsPolygonValidator());
	private final ContinentsNoiseDeformer continentsNoiseDeformer = new ContinentsNoiseDeformer();

	private final BooleanMapScale scaleBackUp = new BooleanMapScale(POLYGON_MAP_DOWNSCALING_FACTOR);


	// GENERATE
	@Override
	public BooleanMap generate(int size, long seed)
	{
		return new GenerateMethodObject(size, new Random(seed)).generate();
	}

	@RequiredArgsConstructor
	private class GenerateMethodObject
	{

		// INPUT
		private final int size;
		private final Random random;


		public BooleanMap generate()
		{
			logger.info("Generate base polygons...");
			int numberOfPolygonsToGenerate = RandomUtil.distribute(3, 1, random);
			List<Polygon2D> polygons = continentsBasePolygonGenerator.generate(random.nextLong(), numberOfPolygonsToGenerate);


			logger.info("Deform polygon shapes...");
			polygons = continentsPolygonDeformer.generate(random.nextLong(), polygons);


			logger.info("Convert polygons to map...");
			BooleanMapFromPolygons generator = new BooleanMapFromPolygons(
					size/(downscalingFactor*POLYGON_MAP_DOWNSCALING_FACTOR));
			BooleanMap continentShape = generator.generate(polygons);
			continentShape = scaleBackUp.transform(continentShape);


			logger.info("Deform continent shape using noise...");
			continentShape = continentsNoiseDeformer.generate(random.nextLong(), continentShape);

			return continentShape;
		}

	}

}
