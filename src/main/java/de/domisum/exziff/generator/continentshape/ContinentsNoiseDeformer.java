package de.domisum.exziff.generator.continentshape;

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
public class ContinentsNoiseDeformer
{

	// SETTINGS
	@Getter
	@Setter
	private int iterations = 2;


	// DEFORM
	public BooleanMap generate(long seed, BooleanMap input)
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
			map = input;

			for(int i = 0; i < iterations; i++)
			{
				scatter();
				cleanup();
			}

			finalCleanup();
			return map;
		}

		private void scatter()
		{
			double d = 0.8;
			double e = 0.7;
			// @formatter:off
			NoiseLayers noiseLayers = new NoiseLayers(
				new NoiseLayer(0.12*d, 0.03*e, -1),
				new NoiseLayer(0.045*d, 0.02*e, -1),
				new NoiseLayer(0.015*d, 0.01*e, -1)
			);
			// @formatter:on

			LayeredOpenSimplexNoise noiseXOffset = new LayeredOpenSimplexNoise(noiseLayers.getRandomSeedsCopy(random));
			LayeredOpenSimplexNoise noiseYOffset = new LayeredOpenSimplexNoise(noiseLayers.getRandomSeedsCopy(random));

			BooleanMapNoiseOffsetter noiseOffsetter = new BooleanMapNoiseOffsetter(noiseXOffset, noiseYOffset);
			map = noiseOffsetter.transform(map);
		}

		private void cleanup()
		{
			cleanup(2, 0.3, 0.35);
		}

		private void cleanup(int radius, double removeThreshold, double addThreshold)
		{
			BooleanMapSmooth smooth = new BooleanMapSmooth(radius, removeThreshold, addThreshold);
			map = smooth.transform(map);

			BooleanMapFloodFill floodFill = new BooleanMapFloodFill();
			map = floodFill.transform(map);

			BooleanMapInvert invert = new BooleanMapInvert();
			map = invert.transform(map);
		}

		private void finalCleanup()
		{
			for(int i = 0; i < 3; i++)
				cleanup(3, 0.32, 0.33);
		}

	}

}
