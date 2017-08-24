package de.domisum.exziff.shape;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.bool.BooleanMapGenerator;
import de.domisum.exziff.map.generator.bool.BooleanMapPolygonGenerator;
import de.domisum.exziff.map.transformation.bool.BooleanMapScale;
import de.domisum.exziff.shape.continent.ContinentShapeBasePolygonGenerator;
import de.domisum.exziff.shape.continent.ContinentShapeNoiseDeformer;
import de.domisum.exziff.shape.continent.ContinentShapePolygonDeformer;
import de.domisum.exziff.shape.continent.ContinentShapePolygonValidator;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ContinentsShapeGenerator extends BooleanMapGenerator
{

	// settings
	private int size;
	@Getter @Setter private int downscalingFactor = 1;
	private int polygonMapDownscalingFactor = 4;

	// REFERENCES
	private ContinentShapeBasePolygonGenerator basePolygonGenerator;
	private ContinentShapePolygonDeformer polygonDeformer;
	private BooleanMapScale scale = new BooleanMapScale(this.polygonMapDownscalingFactor);
	private ContinentShapeNoiseDeformer noiseDeformer;


	// INIT
	public ContinentsShapeGenerator(int size, long seed)
	{
		this.size = size;

		ContinentShapePolygonValidator polygonValidator = new ContinentShapePolygonValidator();

		this.basePolygonGenerator = new ContinentShapeBasePolygonGenerator(seed, polygonValidator);
		this.polygonDeformer = new ContinentShapePolygonDeformer(seed, polygonValidator);
		this.noiseDeformer = new ContinentShapeNoiseDeformer(seed);
	}


	// GENERATE
	@Override public BooleanMap generate()
	{
		// generate using polygons
		List<Polygon2D> polygons = this.basePolygonGenerator.generate();
		polygons = this.polygonDeformer.deformPolygons(polygons);

		BooleanMapPolygonGenerator generator = new BooleanMapPolygonGenerator(
				this.size/(this.downscalingFactor*this.polygonMapDownscalingFactor), polygons);
		BooleanMap continentShape = generator.generate();

		continentShape = this.scale.transform(continentShape);

		// deform
		continentShape = this.noiseDeformer.deform(continentShape);

		return continentShape;
	}

}
