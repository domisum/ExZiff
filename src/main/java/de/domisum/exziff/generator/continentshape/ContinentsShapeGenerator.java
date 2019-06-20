package de.domisum.exziff.generator.continentshape;

import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
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
public class ContinentsShapeGenerator implements RandomizedGeneratorOneInput<Integer, BooleanMap>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	private static final int POLYGON_MAP_DOWNSCALING_FACTOR = 4;

	// SETTINGS
	@Getter
	@Setter
	private int downscalingFactor = 1;

	// REFERENCES
	private final RandomizedGeneratorOneInput<Integer, List<Polygon2D>> basePolygonGenerator;
	private final RandomizedGeneratorOneInput<List<Polygon2D>, List<Polygon2D>> polygonDeformer;
	private final RandomizedGeneratorOneInput<BooleanMap, BooleanMap> noiseDeformer;

	private final BooleanMapScale scaleBackUp = new BooleanMapScale(POLYGON_MAP_DOWNSCALING_FACTOR);


	// INIT
	public ContinentsShapeGenerator()
	{
		ContinentsPolygonValidator polygonValidator = new ContinentsPolygonValidator();

		basePolygonGenerator = new ContinentsBasePolygonGenerator(polygonValidator);
		polygonDeformer = new ContinentsPolygonDeformer(polygonValidator);
		noiseDeformer = new ContinentsNoiseDeformer();
	}


	// GENERATE
	@Override
	public BooleanMap generate(long seed, Integer size)
	{
		return new GenerateMethodObject(new Random(seed), size).generate();
	}

	@RequiredArgsConstructor
	private class GenerateMethodObject
	{

		// INPUT
		private final Random random;
		private final int size;


		public BooleanMap generate()
		{
			logger.info("Generate base polygons...");
			int numberOfPolygonsToGenerate = RandomUtil.distribute(3, 1, random);
			List<Polygon2D> polygons = basePolygonGenerator.generate(random.nextLong(), numberOfPolygonsToGenerate);


			logger.info("Deform polygon shapes...");
			polygons = polygonDeformer.generate(random.nextLong(), polygons);


			logger.info("Convert polygons to map...");
			BooleanMapFromPolygons generator = new BooleanMapFromPolygons(
					size/(downscalingFactor*POLYGON_MAP_DOWNSCALING_FACTOR));
			BooleanMap continentShape = generator.generate(polygons);
			continentShape = scaleBackUp.transform(continentShape);


			logger.info("Deform continent shape using noise...");
			continentShape = noiseDeformer.generate(random.nextLong(), continentShape);

			return continentShape;
		}

	}

}