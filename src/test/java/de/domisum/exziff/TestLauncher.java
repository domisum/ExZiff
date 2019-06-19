package de.domisum.exziff;

import de.domisum.exziff.blockstructure.BlockCoordinate;
import de.domisum.exziff.world.block.Axis;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import de.domisum.lib.auxilium.util.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TestLauncher
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	public static void main(String[] args)
	{
		Map<BlockCoordinate, Block> blockMap = new HashMap<>();
		blockMap.put(new BlockCoordinate(3, 7, 3), new BlockBuilder(Material.LOG_SPRUCE).set(Axis.Y).build());

		byte[] bytes = SerializationUtil.serialize(blockMap);
	}

}
