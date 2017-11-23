package de.domisum.exziff.generator.bedrockpartition;

import de.domisum.exziff.generator.RandomizedGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import lombok.RequiredArgsConstructor;

import java.util.Random;

public class BedrockRegionPartitionGenerator implements RandomizedGenerator<BooleanMap, ShortMap>
{

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


		public ShortMap generate()
		{
			return null;
		}

	}

}
