package de.domisum.exziff.generator.bedrockpartition;

import de.domisum.exziff.generator.RandomizedGeneratorOneInput;
import de.domisum.exziff.map.FloatMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.map.floatmap.FloatMapInMemoryArray;
import de.domisum.exziff.map.floatmap.FloatMapLocalized;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BedrockRegionInfluenceMapGenerator implements RandomizedGeneratorOneInput<ShortMap, Map<Short, FloatMap>>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// GENERATE
	@Override public Map<Short, FloatMap> generate(long seed, ShortMap regionMap)
	{
		return new GenerationMethodObject(regionMap, seed).generate();
	}


	@RequiredArgsConstructor
	private class GenerationMethodObject
	{

		// CONSTANTS
		private static final int INFLUENCE_RADIUS = 15;

		// INPUT
		private final ShortMap regionMap;
		private final long seed;

		// TEMP
		private Map<Short, FloatMap> influenceMaps = new HashMap<>();


		public Map<Short, FloatMap> generate()
		{
			// ocean
			influenceMaps.put((short) 0, new FloatMapInMemoryArray(regionMap.getWidth(), regionMap.getHeight()));

			for(int y = 0; y < regionMap.getHeight(); y++)
			{
				for(int x = 0; x < regionMap.getWidth(); x++)
					processInfluenceAt(x, y);

				System.out.println("y: "+y);
			}

			return influenceMaps;
		}

		private void processInfluenceAt(int x, int y)
		{
			setInfluenceAt(x, y, regionMap.get(x, y), 1.0f);

			if(doesBlockHaveOtherRegionNeighbors(x, y))
				spreadInfluenceFrom(x, y);
		}

		private void spreadInfluenceFrom(int x, int y)
		{
			for(int dY = -INFLUENCE_RADIUS; dY <= INFLUENCE_RADIUS; dY++)
				for(int dX = -INFLUENCE_RADIUS; dX <= INFLUENCE_RADIUS; dX++)
				{
					int nX = x+dX;
					int nY = y+dY;

					if(!isInBounds(nX, nY))
						continue;

					double distanceFromCenter = Math.sqrt(dX*dX+dY*dY);
					double influenceStrength = 1-(distanceFromCenter/INFLUENCE_RADIUS);

					if(influenceStrength <= 0)
						continue;

					setInfluenceAt(nX, nY, regionMap.get(x, y), (float) influenceStrength);
				}
		}

		private void setInfluenceAt(int x, int y, short region, float influence)
		{
			if(!influenceMaps.containsKey(region))
				influenceMaps.put(region, new FloatMapLocalized(regionMap.getWidth(), regionMap.getHeight()));
			FloatMap influenceMap = influenceMaps.get(region);

			if(influenceMap.get(x, y) < influence)
				influenceMap.set(x, y, influence);
		}


		// UTIL
		private boolean doesBlockHaveOtherRegionNeighbors(int x, int y)
		{
			short regionAt = regionMap.get(x, y);

			if(isNeighborDifferent(x+1, y, regionAt))
				return true;

			if(isNeighborDifferent(x-1, y, regionAt))
				return true;

			if(isNeighborDifferent(x, y+1, regionAt))
				return true;

			if(isNeighborDifferent(x, y-1, regionAt))
				return true;

			return false;
		}

		private boolean isNeighborDifferent(int nX, int nY, short baseRegion)
		{
			if(!isInBounds(nX, nY))
				return false;

			return regionMap.get(nX, nY) != baseRegion;
		}

		private boolean isInBounds(int x, int y)
		{
			if(x < 0 || x >= regionMap.getWidth())
				return false;

			if(y < 0 || y >= regionMap.getHeight())
				return false;

			return true;
		}

	}

}
