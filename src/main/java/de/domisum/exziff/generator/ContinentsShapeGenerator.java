package de.domisum.exziff.generator;

import de.domisum.exziff.generator.continentshape.ContinentsBasePolygonGenerator;
import de.domisum.exziff.generator.continentshape.ContinentsNoiseDeformer;
import de.domisum.exziff.generator.continentshape.ContinentsPolygonDeformer;
import de.domisum.exziff.generator.continentshape.ContinentsPolygonValidator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.bool.BooleanMapFromPolygons;
import de.domisum.exziff.map.generator.bool.BooleanMapGenerator;
import de.domisum.exziff.map.transformation.bool.BooleanMapScale;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * Generates random continent shapes from a given seed.
 */
public class ContinentsShapeGenerator extends BooleanMapGenerator
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// settings
	private int size;
	private Random random;
	@Getter @Setter private int downscalingFactor = 1;
	private int polygonMapDownscalingFactor = 4;

	// REFERENCES
	private RandomizedGenerator<Integer, List<Polygon2D>> basePolygonGenerator;
	private RandomizedGenerator<List<Polygon2D>, List<Polygon2D>> polygonDeformer;
	private ContinentsNoiseDeformer noiseDeformer;

	private BooleanMapScale scaleBackUp = new BooleanMapScale(this.polygonMapDownscalingFactor);


	// INIT
	public ContinentsShapeGenerator(int size, long seed)
	{
		this.size = size;
		this.random = new Random(seed);

		ContinentsPolygonValidator polygonValidator = new ContinentsPolygonValidator();
		this.basePolygonGenerator = new ContinentsBasePolygonGenerator(polygonValidator);
		this.polygonDeformer = new ContinentsPolygonDeformer(polygonValidator);
		this.noiseDeformer = new ContinentsNoiseDeformer();
	}


	// GENERATE
	@Override public BooleanMap generate()
	{
		// generate using polygons
		this.logger.info("Generate base polygons...");
		int numberOfPolygonsToGenerate = RandomUtil.distribute(3, 1, random);
		List<Polygon2D> polygons = this.basePolygonGenerator.generate(random.nextLong(), numberOfPolygonsToGenerate);

		this.logger.info("Deform polygon shapes...");
		polygons = this.polygonDeformer.generate(random.nextLong(), polygons);

		this.logger.info("Convert polygons to map...");
		BooleanMapFromPolygons generator = new BooleanMapFromPolygons(
				this.size/(this.downscalingFactor*this.polygonMapDownscalingFactor), polygons);
		BooleanMap continentShape = generator.generate();

		continentShape = this.scaleBackUp.transform(continentShape);


		this.logger.info("Deform continent shape using noise...");
		continentShape = this.noiseDeformer.generate(random.nextLong(), continentShape);

		return continentShape;
	}

}
