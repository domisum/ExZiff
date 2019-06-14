package de.domisum.exziff.blockstructure;

import de.domisum.exziff.world.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BlockStructure
{

	private final Map<BlockCoordinate, Block> blocks = new HashMap<>();


	// GETTERS
	public Optional<Block> getBlock(BlockCoordinate blockCoordinate)
	{
		return Optional.ofNullable(blocks.get(blockCoordinate));
	}

	public Optional<Block> getBlock(int x, int y, int z)
	{
		return getBlock(new BlockCoordinate(x, y, z));
	}

	public Set<BlockCoordinate> getBlockCoordinates()
	{
		return blocks.keySet();
	}


	// SETTERS
	public void setBlock(int x, int y, int z, Block block)
	{
		blocks.put(new BlockCoordinate(x, y, z), block);
	}

}
