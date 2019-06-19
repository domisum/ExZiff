package de.domisum.exziff;

import de.domisum.exziff.world.World;
import de.domisum.exziff.world.block.Axis;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import de.domisum.exziff.world.chunkclusterstorage.ChunkClusterStorageFromDisk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TestLauncher
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	public static void main(String[] args)
	{
		World world = new World(new ChunkClusterStorageFromDisk(new File("C:\\Users\\domisum\\testChamber\\exziff\\worlds\\test"),
				true
		));

		Block spruceLog = new BlockBuilder(Material.LOG_SPRUCE).set(Axis.X).build();
		Block acaciaLeaves = new BlockBuilder(Material.LEAVES_ACACIA).build();

		world.setBlock(0, 0, 0, spruceLog);
		world.setBlock(2, 3, 0, spruceLog);
		world.setBlock(0, 0, 0, acaciaLeaves);

		System.out.println(world.getBlock(0, 0, 0));

		world.save();
	}

}
