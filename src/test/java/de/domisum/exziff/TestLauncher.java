package de.domisum.exziff;

import de.domisum.exziff.generator.WorldGenerator;
import de.domisum.exziff.prototype.Prototype;

import java.io.File;

public class TestLauncher
{

	public static void main(String[] args)
	{
		Prototype.partitioningTest();

		if(System.currentTimeMillis() > 0)
			return;

		WorldGenerator generator = new WorldGenerator((int) Math.pow(2, 14), 1338,
				new File("C:\\Users\\domisum\\testChamber\\exziff\\testWorld"));
		generator.generate();
	}

}
