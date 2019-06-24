package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegion;
import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.blockstack.BlockStack;
import de.domisum.exziff.map.FloatMap;
import de.domisum.exziff.world.block.Material;

public class OceanFloor extends BedrockRegion
{

	// INIT
	public OceanFloor(int id, long seed, FloatMap influenceMap)
	{
		super(BedrockRegionType.OCEAN_FLOOR, id, seed, influenceMap);
	}


	// GENERATION
	@Override
	public void preGenerate()
	{
		// for now ocean floor is just flat
	}

	@Override
	public BlockStack getBlockStackAt(int x, int z)
	{
		return BlockStack.fromMaterialAndMaxY(Material.STONE, 30);
	}

}
