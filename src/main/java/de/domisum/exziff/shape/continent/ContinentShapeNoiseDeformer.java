package de.domisum.exziff.shape.continent;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.transformation.bool.BooleanMapFloodFill;
import de.domisum.exziff.map.transformation.bool.BooleanMapInvert;
import de.domisum.exziff.map.transformation.bool.BooleanMapSmooth;
import de.domisum.exziff.map.transformation.bool.noise.BooleanMapNoiseOffsetter;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.layeredopensimplexnoise.NoiseLayers;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class ContinentShapeNoiseDeformer
{

	// SETTINGS
	@Getter @Setter private int iterations = 3;

	// REFERENCES
	private Random random;

	// TEMP
	private BooleanMap map;


	// INIT
	public ContinentShapeNoiseDeformer(long seed)
	{
		this.random = new Random(seed);
	}


	// DEFORMING
	public synchronized BooleanMap deform(BooleanMap input)
	{
		this.map = input;

		for(int i = 0; i < this.iterations; i++)
		{
			scatter(i);
			cleanup();
		}

		return this.map;
	}

	private void scatter(int iteration)
	{
		NoiseLayers noiseLayers = new NoiseLayers(new NoiseLayer(0.03, 0.01, -1));

		LayeredOpenSimplexNoise noiseXOffset = new LayeredOpenSimplexNoise(noiseLayers.getRandomSeedsCopy(this.random));
		LayeredOpenSimplexNoise noiseYOffset = new LayeredOpenSimplexNoise(noiseLayers.getRandomSeedsCopy(this.random));


		BooleanMapNoiseOffsetter noiseOffsetter = new BooleanMapNoiseOffsetter(noiseXOffset, noiseYOffset);
		this.map = noiseOffsetter.transform(this.map);
	}

	private void cleanup()
	{
		BooleanMap deformedShape = this.map;

		BooleanMapSmooth smooth = new BooleanMapSmooth(2, 0.3, 0.35);
		deformedShape = smooth.transform(deformedShape);

		BooleanMapFloodFill floodFill = new BooleanMapFloodFill();
		deformedShape = floodFill.transform(deformedShape);

		BooleanMapInvert invert = new BooleanMapInvert();
		deformedShape = invert.transform(deformedShape);

		this.map = deformedShape;
	}

}
