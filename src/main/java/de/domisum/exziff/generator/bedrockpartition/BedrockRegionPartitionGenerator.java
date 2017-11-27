package de.domisum.exziff.generator.bedrockpartition;

import de.domisum.exziff.bedrockregion.BedrockRegionMap;
import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
public class BedrockRegionPartitionGenerator implements RandomizedGeneratorOneInput<BooleanMap, BedrockRegionMap>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// GENERATE
	@Override public BedrockRegionMap generate(long seed, BooleanMap continentShape)
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
		private ShortMap regionMap;
		private List<Short> usedRegionIds;


		// GENERATE
		public BedrockRegionMap generate()
		{
			generateRegionMap();
			determineUsedRegionIds();

			return new BedrockRegionMap(regionMap);
		}


		private void generateRegionMap()
		{
			logger.info("Generating region map...");

			RandomizedGeneratorOneInput<BooleanMap, ShortMap> nearestPointPartitionGenerator = new NearestPointPartitionGenerator(
					new UniformlyDistributedPointsGenerator());
			regionMap = nearestPointPartitionGenerator.generate(random.nextLong(), continentShape);
		}

		private void determineUsedRegionIds()
		{
			logger.info("Determining used regionIds...");

			Set<Short> usedRegionIdsNoDuplicates = new HashSet<>();
			for(int y = 0; y < regionMap.getHeight(); y++)
				for(int x = 0; x < regionMap.getWidth(); x++)
					if(regionMap.get(x, y) != 0) // OCEAN
						usedRegionIdsNoDuplicates.add(regionMap.get(x, y));

			usedRegionIds = new ArrayList<>(usedRegionIdsNoDuplicates);
			usedRegionIds.sort(Short::compareTo);

			logger.info("Found: {}", usedRegionIds);
		}

	}

}

