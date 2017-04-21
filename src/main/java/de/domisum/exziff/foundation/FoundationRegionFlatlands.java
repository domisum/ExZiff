package de.domisum.exziff.foundation;

public class FoundationRegionFlatlands extends FoundationRegion
{

	// INIT
	public FoundationRegionFlatlands(int id, long seed)
	{
		super(FoundationRegionType.FLATLANDS, id, seed);
	}


	// GENERATION
	@Override public void generate()
	{

	}

	@Override public int getHeightAt(int x, int z)
	{
		return 70;
	}

}
