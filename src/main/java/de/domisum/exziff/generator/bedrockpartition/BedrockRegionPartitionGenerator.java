package de.domisum.exziff.generator.bedrockpartition;

import de.domisum.exziff.bedrockregion.BedrockRegionMap;
import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.bedrockregion.regions.BedrockRegion;
import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.FloatMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.lib.auxilium.util.math.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		private Map<Short, FloatMap> regionInfluenceMaps;
		private Set<BedrockRegion> bedrockRegions = new HashSet<>();


		// GENERATE
		public BedrockRegionMap generate()
		{
			generateRegionMap();

			determineUsedRegionIds();
			determineRegionInfluence();
			generateRegions();

			return new BedrockRegionMap(regionMap, bedrockRegions);
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

		private void determineRegionInfluence()
		{
			BedrockRegionInfluenceMapGenerator bedrockRegionInfluenceMapGenerator = new BedrockRegionInfluenceMapGenerator();
			regionInfluenceMaps = bedrockRegionInfluenceMapGenerator.generate(random.nextLong(), regionMap);
		}

		private void generateRegions()
		{
			generateOceanRegion();
			generateNonOceanRegions();
		}

		private void generateOceanRegion()
		{
			BedrockRegion ocean = BedrockRegionType.OCEAN_FLOOR
					.getInstance(0, random.nextLong(), regionInfluenceMaps.get((short) 0));
			bedrockRegions.add(ocean);
		}

		private void generateNonOceanRegions()
		{
			Set<BedrockRegionType> types = new HashSet<>(Arrays.asList(BedrockRegionType.values()));
			types.remove(BedrockRegionType.OCEAN_FLOOR);

			Set<Short> nonOceanRegions = new HashSet<>(usedRegionIds);
			nonOceanRegions.remove((short) 0);
			for(short s : nonOceanRegions)
			{
				BedrockRegionType randomType = RandomUtil.getElement(types, random);

				BedrockRegion bedrockRegion = randomType.getInstance(s, random.nextLong(), regionInfluenceMaps.get(s));
				bedrockRegions.add(bedrockRegion);
			}
		}

	}

}

