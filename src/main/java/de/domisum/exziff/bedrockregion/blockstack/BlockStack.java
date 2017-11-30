package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.world.Material;

public interface BlockStack
{

	Material getMaterialAt(int y);

	int getMaxHeight();


	// INIT
	static BlockStack fromMaterialArray(Material[] materials)
	{
		return new BlockStackSimple(materials);
	}

}
