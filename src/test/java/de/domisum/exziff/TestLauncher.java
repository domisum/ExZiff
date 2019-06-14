package de.domisum.exziff;

import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.HorizontalOrientation;
import de.domisum.exziff.world.block.Material;
import de.domisum.exziff.world.block.OptionalAxis;
import de.domisum.exziff.world.block.VerticalOrientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLauncher
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	public static void main(String[] args)
	{
		Block grass = new BlockBuilder(Material.GRASS).build();
		Block log = new BlockBuilder(Material.LOG_OAK).set(OptionalAxis.class, OptionalAxis.NONE).build();
		Block stair = new BlockBuilder(Material.STAIR_OAK)
				.set(HorizontalOrientation.class, HorizontalOrientation.BOTTOM)
				.set(VerticalOrientation.class, VerticalOrientation.TOWARD_POSITIVE_X)
				.build();
	}

}
