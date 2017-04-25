package de.domisum.exziff.foundation;

import de.domisum.exziff.map.FloatMap;
import lombok.Getter;
import lombok.Setter;

public abstract class FoundationRegion
{

	@Getter protected final FoundationRegionType type;
	@Getter protected final int id;
	protected final long seed;

	@Setter protected FloatMap influenceMap;


	// INIT
	public FoundationRegion(FoundationRegionType type, int id, long seed)
	{
		this.type = type;
		this.id = id;
		this.seed = seed;
	}


	// GENERATION
	public abstract void generate();

	public abstract int getHeightAt(int x, int z);

}
