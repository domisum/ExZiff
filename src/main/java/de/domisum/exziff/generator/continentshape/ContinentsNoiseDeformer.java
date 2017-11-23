package de.domisum.exziff.generator.continentshape;

import de.domisum.exziff.generator.RandomizedGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.transformer.bool.BooleanMapFloodFill;
import de.domisum.exziff.map.transformer.bool.BooleanMapInvert;
import de.domisum.exziff.map.transformer.bool.BooleanMapNoiseOffsetter;
import de.domisum.exziff.map.transformer.bool.BooleanMapSmooth;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.layeredopensimplexnoise.NoiseLayers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Random;

/**
 * Deforms the rough continent shapes to look more organic.
 */
public class ContinentsNoiseDeformer implements RandomizedGenerator<BooleanMap, BooleanMap>
{

	// SETTINGS
	@Getter @Setter private int iterations = 2;


	// DEFORM
	@Override public BooleanMap generate(long seed, BooleanMap input)
	{
		return new GenerateMethodObject(new Random(seed), input).deform();
	}

	@RequiredArgsConstructor
	private class GenerateMethodObject
	{

		// INPUT
		private final Random random;
		private final BooleanMap input;

		// TEMP
		private BooleanMap map;


		// DEFORMING
		public BooleanMap deform()
		{
			this.map = input;

			for(int i = 0; i < iterations; i++)
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

}
