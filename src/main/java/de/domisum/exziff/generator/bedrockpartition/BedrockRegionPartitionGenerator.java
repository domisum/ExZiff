package de.domisum.exziff.generator.bedrockpartition;

import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
public class BedrockRegionPartitionGenerator implements RandomizedGeneratorOneInput<BooleanMap, ShortMap>
{

	// REFERENCES
	private final RandomizedGeneratorOneInput<Integer, Set<Vector2D>> regionCenterPointsGenerator;


	// GENERATE
	@Override public ShortMap generate(long seed, BooleanMap continentShape)
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
		private Set<Vector2D> regionCenterPoints;


		// GENERATE
		public ShortMap generate()
		{
			regionCenterPoints = regionCenterPointsGenerator.generate(random.nextLong(), 300);

			return null;
		}

	}

}

