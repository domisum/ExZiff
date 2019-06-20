package de.domisum.exziff;

import de.domisum.exziff.world.World;
import de.domisum.exziff.world.block.Axis;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import de.domisum.exziff.world.chunkclusterstorage.ChunkClusterStorageFromDisk;
import de.domisum.lib.auxilium.util.time.ProfilerStopWatch;
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
		Block acaciaLeaves = new BlockBuilder(Material.STONE).build();


		for(int xBase = 0; xBase < Math.pow(2, 8); xBase += 16*16)
		{
			ProfilerStopWatch profilerStopWatch = new ProfilerStopWatch("xBase: "+xBase);
			profilerStopWatch.start();

			for(int zBase = 0; zBase < Math.pow(2, 8); zBase += 16*16)
			{
				for(int x = 0; x < (16*16); x++)
					for(int z = 0; z < (16*16); z++)
						if(((x+z)%2) == 0)
							world.setBlock(xBase+x, 0, zBase+z, acaciaLeaves);

				System.out.println("xBase: "+xBase+" zBase: "+zBase);
			}

			System.out.println(profilerStopWatch);
		}

		world.save();
	}

}
