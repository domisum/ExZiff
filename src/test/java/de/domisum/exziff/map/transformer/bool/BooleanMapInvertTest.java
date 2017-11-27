package de.domisum.exziff.map.transformer.bool;

import de.domisum.exziff.map.BooleanMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class BooleanMapInvertTest
{

	@Test public void testInvertPredefined()
	{
		invertAndAssertInverted(new BooleanMap(30, 8));
		invertAndAssertInverted(new BooleanMap(1, 1));
	}

	@Test public void testInvertRandomized()
	{
		int sizeMax = 500;
		Random random = new Random(0xaffe8);

		for(int i = 0; i < 30; i++)
			invertAndAssertInverted(generateRandomBooleanMap(1+random.nextInt(sizeMax), 1+random.nextInt(sizeMax), random));
	}


	// ARRANGE
	private static BooleanMap generateRandomBooleanMap(int width, int height, Random random)
	{
		BooleanMap booleanMap = new BooleanMap(width, height);

		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				booleanMap.set(x, y, random.nextBoolean());

		return booleanMap;
	}

	// ACT
	private static void invertAndAssertInverted(BooleanMap booleanMap)
	{
		BooleanMapInvert booleanMapInvert = new BooleanMapInvert();

		BooleanMap inverted = booleanMapInvert.transform(booleanMap);
		assertInverted(booleanMap, inverted);
	}

	// ASSERT
	private static void assertInverted(BooleanMap base, BooleanMap inverted)
	{
		Assertions.assertEquals(base.getWidth(), inverted.getWidth(), "width not equal");
		Assertions.assertEquals(base.getHeight(), inverted.getHeight(), "height not equal");

		for(int x = 0; x < base.getWidth(); x++)
			for(int y = 0; y < base.getHeight(); y++)
				Assertions.assertEquals(base.get(x, y), !inverted.get(x, y), "not inverted at x"+x+" y"+y);
	}

}
