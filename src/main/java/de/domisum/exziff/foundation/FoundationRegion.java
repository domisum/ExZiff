package de.domisum.exziff.foundation;

import de.domisum.lib.auxilium.data.container.IntBounds2D;
import lombok.Getter;

public abstract class FoundationRegion
{

	@Getter private final FoundationRegionType type;
	@Getter private final int id;
	private final long seed;

	private IntBounds2D bounds;


	// INIT
	public FoundationRegion(FoundationRegionType type, int id, long seed)
	{
		this.type = type;
		this.id = id;
		this.seed = seed;
	}


	// SETTERS
	public void setBounds(IntBounds2D bounds)
	{
		this.bounds = bounds;
	}

	// GENERATION
	public abstract void generate();

	public abstract int getHeightAt(int x, int z);

}
