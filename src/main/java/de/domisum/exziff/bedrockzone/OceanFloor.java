package de.domisum.exziff.bedrockzone;

public class OceanFloor extends BedrockZone
{

	// INIT
	public OceanFloor(int id, long seed)
	{
		super(BedrockZoneType.OCEAN_FLOOR, id, seed);
	}


	// GENERATION
	@Override public void generate()
	{
		// for now ocean floor is just flat
	}

	@Override public int getHeightAt(int x, int z)
	{
		return 40;
	}

}
