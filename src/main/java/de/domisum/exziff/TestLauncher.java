package de.domisum.exziff;

import java.io.File;

public class TestLauncher
{

	public static void main(String[] args)
	{
		worldGeneratorTest();
	}

	private static void worldGeneratorTest()
	{
		File worldDir = new File("C:\\Users\\domisum\\testChamber\\exziff\\testWorld");

		WorldGenerator generator = new WorldGenerator(2048, 1337, worldDir);
		generator.generate();

		System.out.println("done");
	}

}
