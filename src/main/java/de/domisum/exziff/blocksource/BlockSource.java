package de.domisum.exziff.blocksource;

import de.domisum.exziff.blockstructure.BlockCoordinate;
import de.domisum.exziff.world.block.Block;

public interface BlockSource
{

	Block get(int x, int y, int z);


	default Block get(BlockCoordinate blockCoordinate)
	{
		return get(blockCoordinate.getX(), blockCoordinate.getY(), blockCoordinate.getZ());
	}

}
