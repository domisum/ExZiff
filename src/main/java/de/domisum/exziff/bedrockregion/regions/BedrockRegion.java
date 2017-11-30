package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.bedrockregion.blockstack.BlockStack;
import de.domisum.exziff.map.FloatMap;
import lombok.Getter;

import java.util.Random;

public abstract class BedrockRegion
{

	// properties
	@Getter protected final BedrockRegionType type;
	@Getter protected final int id;

	@Getter protected FloatMap influenceMap;

	protected final Random random;


	// INIT
	protected BedrockRegion(BedrockRegionType type, int id, long seed, FloatMap influenceMap)
	{
		this.type = type;
		this.id = id;

		this.influenceMap = influenceMap;

		this.random = new Random(seed);
	}


	// GENERATION
	public abstract void generate();

	public abstract BlockStack getBlockStackAt(int x, int z);

}
