package de.domisum.exziff.bedrockregion;

import de.domisum.exziff.bedrockregion.regions.BedrockRegion;
import de.domisum.exziff.map.ShortMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RequiredArgsConstructor
public class BedrockRegionMap
{

	@Getter private final ShortMap regionIdMap;
	private final Collection<BedrockRegion> bedrockRegions;


	// GETTERS
	public Collection<BedrockRegion> getBedrockRegions()
	{
		return new HashSet<>(bedrockRegions);
	}

	public Map<BedrockRegion, Float> getInfluencesAt(int x, int y)
	{
		Map<BedrockRegion, Float> influencesAt = new HashMap<>();
		for(BedrockRegion bedrockRegion : bedrockRegions)
			if(bedrockRegion.getInfluenceMap().get(x, y) > 0)
				influencesAt.put(bedrockRegion, bedrockRegion.getInfluenceMap().get(x, y));

		return influencesAt;
	}

}
