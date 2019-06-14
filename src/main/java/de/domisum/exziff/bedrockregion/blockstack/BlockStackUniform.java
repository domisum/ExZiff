package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.oldworld.Material;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockStackUniform implements BlockStack
{

	// INPUT
	private final Material material;
	private final int maximumY;


	// GETTERS
	@Override public Material getMaterialAt(int y)
	{
		if(y <= maximumY)
			return material;

		return DEFAULT_MATERIAL;
	}

	@Override public int getMaximumY()
	{
		return maximumY;
	}

}
