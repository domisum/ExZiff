package de.domisum.exziff.bedrockregion;

import de.domisum.exziff.blockstack.BlockStack;
import de.domisum.exziff.map.FloatMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Random;

@EqualsAndHashCode(of = {"type", "id"})
public abstract class BedrockRegion
{

	// properties
	@Getter
	protected final BedrockRegionType type;
	@Getter
	protected final int id;
	@Getter
	protected FloatMap influenceMap;
	protected long seed;

	protected final Random random;


	// INIT
	protected BedrockRegion(BedrockRegionType type, int id, long seed, FloatMap influenceMap)
	{
		this.type = type;
		this.id = id;
		this.influenceMap = influenceMap;
		this.seed = seed;

		random = new Random(seed);
	}


	// GENERATION
	public abstract void preGenerate();

	public abstract BlockStack getBlockStackAt(int x, int z);

}

