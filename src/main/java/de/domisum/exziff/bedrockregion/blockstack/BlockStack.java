package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.world.Material;

public interface BlockStack
{

	// CONSTANTS
	Material DEFAULT_MATERIAL = Material.AIR;


	Material getMaterialAt(int y);

	int getMaximumY();


	// INIT
	static BlockStack fromMaterialArray(Material[] materials)
	{
		return new BlockStackSimple(materials);
	}

	static BlockStack fromMaterialAndMaxY(Material material, int maxY)
	{
		return new BlockStackUniform(material, maxY);
	}

}
