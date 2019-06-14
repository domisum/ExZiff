package de.domisum.exziff.blockstructure;

import de.domisum.exziff.world.block.Block;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BlockStructure
{

	private final Map<BlockCoordinate, Block> blocks = new HashMap<>();


	// GETTERS
	public Optional<Block> getBlock(int x, int y, int z)
	{
		return Optional.ofNullable(blocks.get(new BlockCoordinate(x, y, z)));
	}

	// SETTERS
	public void setBlock(int x, int y, int z, Block block)
	{
		blocks.put(new BlockCoordinate(x, y, z), block);
	}


	// UTIL
	@RequiredArgsConstructor
	@EqualsAndHashCode
	@ToString
	private static class BlockCoordinate
	{

		@Getter
		private final int x;

		@Getter
		private final int y;

		@Getter
		private final int z;

	}

}
