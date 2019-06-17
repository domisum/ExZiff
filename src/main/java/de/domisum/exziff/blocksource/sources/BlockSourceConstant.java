package de.domisum.exziff.blocksource.sources;

import de.domisum.exziff.blocksource.BlockSource;
import de.domisum.exziff.world.block.Block;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockSourceConstant implements BlockSource
{

	private final Block block;


	// GETTERS
	@Override
	public Block get(int x, int y, int z)
	{
		return block;
	}

}
