package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.world.Material;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockStackSimple implements BlockStack
{

	// CONSTANTS
	private static final Material DEFAULT_MATERIAL = Material.AIR;

	// INPUT
	private final Material[] materials;

	// TEMP
	private Integer _maxHeight = null;


	// GETTERS
	@Override public Material getMaterialAt(int y)
	{
		if(y >= materials.length)
			return DEFAULT_MATERIAL;

		return materials[y];
	}

	@Override public int getMaxHeight()
	{
		if(_maxHeight == null)
			determineMaxHeight();

		return _maxHeight;
	}

	private void determineMaxHeight()
	{
		for(int y = materials.length-1; y >= 0; y--)
			if(getMaterialAt(y) != Material.AIR)
				_maxHeight = y+1;

		_maxHeight = -1;
	}

}
