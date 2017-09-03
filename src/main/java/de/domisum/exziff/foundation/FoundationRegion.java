package de.domisum.exziff.foundation;

import de.domisum.exziff.map.FloatMap;
import de.domisum.lib.auxilium.data.container.bound.IntBounds2D;
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
	@Setter protected IntBounds2D bounds;

	protected final Random seedRandom;


	// INIT
	protected FoundationRegion(FoundationRegionType type, int id, long seed)
	{
		this.type = type;
		this.id = id;
		this.seed = seed;

		// improving randomness by salting
		this.seedRandom = new Random(seed*id*29349349394L);
		this.seedRandom.nextLong();
		this.seedRandom.nextLong();
	}


	// GENERATION
	public abstract void generate();

	public abstract int getHeightAt(int x, int z);

}
