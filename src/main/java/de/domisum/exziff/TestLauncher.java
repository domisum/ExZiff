package de.domisum.exziff;

import de.domisum.exziff.world.World;
import de.domisum.lib.auxilium.util.FileUtil;

import java.io.File;

public class TestLauncher
{

	public static void main(String[] args)
	{
		//islandTest();
		//worldTest();

		//worldGeneratorTest();
	}

	public static void worldGeneratorTest()
	{
		File worldFolder = new File("C:/Users/domisum/testChamber/testWorld");
		FileUtil.deleteDirectory(worldFolder);

		WorldGenerator worldGenerator = new WorldGenerator(1024*2, 8798989&1823305, worldFolder);
		World world = worldGenerator.generate();
	}

}
