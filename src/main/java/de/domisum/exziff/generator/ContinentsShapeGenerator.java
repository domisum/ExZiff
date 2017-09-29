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
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generates random continent shapes from a given seed.
 */
public class ContinentsShapeGenerator extends BooleanMapGenerator
{

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// settings
	private int size;
	@Getter @Setter private int downscalingFactor = 1;
	private int polygonMapDownscalingFactor = 4;

	// REFERENCES
	private ContinentsBasePolygonGenerator basePolygonGenerator;
	private ContinentsPolygonDeformer polygonDeformer;
	private BooleanMapScale scaleBackUp = new BooleanMapScale(this.polygonMapDownscalingFactor);
	private ContinentsNoiseDeformer noiseDeformer;


	// INIT
	public ContinentsShapeGenerator(int size, long seed)
	{
		this.size = size;

		ContinentsPolygonValidator polygonValidator = new ContinentsPolygonValidator();

		this.basePolygonGenerator = new ContinentsBasePolygonGenerator(seed, polygonValidator);
		this.polygonDeformer = new ContinentsPolygonDeformer(seed, polygonValidator);
		this.noiseDeformer = new ContinentsNoiseDeformer(seed);
	}


	// GENERATE
	@Override public BooleanMap generate()
	{
		// generate using polygons
		this.logger.info("Generate base polygons...");
		List<Polygon2D> polygons = this.basePolygonGenerator.generate();

		this.logger.info("Deform polygon shapes...");
		polygons = this.polygonDeformer.deformPolygons(polygons);

		this.logger.info("Convert polygons to map...");
		BooleanMapFromPolygons generator = new BooleanMapFromPolygons(
				this.size/(this.downscalingFactor*this.polygonMapDownscalingFactor), polygons);
		BooleanMap continentShape = generator.generate();

		continentShape = this.scaleBackUp.transform(continentShape);

		this.logger.info("Deform continent shape using noise...");
		continentShape = this.noiseDeformer.deform(continentShape);

		return continentShape;
	}

}
