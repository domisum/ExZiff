package de.domisum.exziff.generator.continentshape;

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

public class ContinentsNoiseDeformer
{

	// SETTINGS
	@Getter @Setter private int iterations = 2;

	// REFERENCES
	private Random random;

	// TEMP
	private BooleanMap map;


	// INIT
	public ContinentsNoiseDeformer(long seed)
	{
		this.random = new Random(seed);
	}


	// DEFORMING
	public synchronized BooleanMap deform(BooleanMap input)
	{
		this.map = input;

		for(int i = 0; i < this.iterations; i++)
		{
			scatter();
			cleanup();
		}

		finalCleanup();
		return this.map;
	}

	private void scatter()
	{
		// @formatter:off
		NoiseLayers noiseLayers = new NoiseLayers(
				new NoiseLayer(0.08, 0.01, -1),
				new NoiseLayer(0.03, 0.01, -1),
				new NoiseLayer(0.03*0.35, 0.01*0.4, -1)
		);
		// @formatter:on

		LayeredOpenSimplexNoise noiseXOffset = new LayeredOpenSimplexNoise(noiseLayers.getRandomSeedsCopy(this.random));
		LayeredOpenSimplexNoise noiseYOffset = new LayeredOpenSimplexNoise(noiseLayers.getRandomSeedsCopy(this.random));

		BooleanMapNoiseOffsetter noiseOffsetter = new BooleanMapNoiseOffsetter(noiseXOffset, noiseYOffset);
		this.map = noiseOffsetter.transform(this.map);
	}

	private void cleanup()
	{
		cleanup(2, 0.3, 0.35);
	}

	private void cleanup(int radius, double removeThreshold, double addThreshold)
	{
		BooleanMapSmooth smooth = new BooleanMapSmooth(radius, removeThreshold, addThreshold);
		this.map = smooth.transform(this.map);

		BooleanMapFloodFill floodFill = new BooleanMapFloodFill();
		this.map = floodFill.transform(this.map);

		BooleanMapInvert invert = new BooleanMapInvert();
		this.map = invert.transform(this.map);
	}

	private void finalCleanup()
	{
		for(int i = 0; i < 3; i++)
			cleanup(3, 0.32, 0.33);
	}

}
