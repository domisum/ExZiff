package de.domisum.exziff.blockstructure;

import de.domisum.exziff.world.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BlockStructure
{

	private final Map<BlockCoordinate, Block> blocks = new HashMap<>();


	// GETTERS
	public Block getBlock(BlockCoordinate blockCoordinate)
	{
		return blocks.get(blockCoordinate);
	}

	public Block getBlock(int x, int y, int z)
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
		BlockCoordinate blockCoordinate = new BlockCoordinate(x, y, z);
		setBlock(blockCoordinate, block);
	}

	public void setBlock(BlockCoordinate blockCoordinate, Block block)
	{
		blocks.put(blockCoordinate, block);
	}

	public void setBlockIfNotSetAlready(int x, int y, int z, Block block)
	{
		setBlockIfNotSetAlready(new BlockCoordinate(x, y, z), block);
	}

	public void setBlockIfNotSetAlready(BlockCoordinate blockCoordinate, Block block)
	{
		if(!blocks.containsKey(blockCoordinate))
			setBlock(blockCoordinate, block);
	}


	public void set(BlockStructure other)
	{
		for(Entry<BlockCoordinate, Block> entry : other.blocks.entrySet())
			setBlock(entry.getKey(), entry.getValue());
	}

	public void setIfNotSetAlready(BlockStructure other)
	{
		for(Entry<BlockCoordinate, Block> entry : other.blocks.entrySet())
			if(!blocks.containsKey(entry.getKey()))
				setBlock(entry.getKey(), entry.getValue());
	}

}
