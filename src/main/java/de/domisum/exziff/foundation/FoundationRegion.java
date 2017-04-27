package de.domisum.exziff.foundation;

import de.domisum.exziff.map.FloatMap;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public abstract class FoundationRegion
{

	// properties
	@Getter protected final FoundationRegionType type;
	@Getter protected final int id;
	protected final long seed;

	@Setter protected FloatMap influenceMap;

	protected final Random seedRandom;


	// INIT
	public FoundationRegion(FoundationRegionType type, int id, long seed)
	{
		this.type = type;
		this.id = id;
		this.seed = seed;

		this.seedRandom = new Random(seed*id*29349349394L);
		this.seedRandom.nextLong();
		this.seedRandom.nextLong();
	}


	// GENERATION
	public abstract void generate();

	public abstract int getHeightAt(int x, int z);

}
