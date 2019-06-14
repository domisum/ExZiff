package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.oldworld.Material;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockStackSimple implements BlockStack
{

	// INPUT
	private final Material[] materials;

	// TEMP
	private Integer _maxY = null;


	// GETTERS
	@Override public Material getMaterialAt(int y)
	{
		if(y >= materials.length)
			return DEFAULT_MATERIAL;

		return materials[y];
	}

	@Override public int getMaximumY()
	{
		if(_maxY == null)
			determineMaxY();

		return _maxY;
	}

	private void determineMaxY()
	{
		for(int y = materials.length-1; y >= 0; y--)
			if(getMaterialAt(y) != Material.AIR)
			{
				_maxY = y;
				return;
			}

		_maxY = -1;
	}

}
