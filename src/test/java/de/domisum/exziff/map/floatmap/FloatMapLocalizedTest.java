package de.domisum.exziff.map.floatmap;

import de.domisum.exziff.map.FloatMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class FloatMapLocalizedTest
{

	private FloatMap normalFloatMap;
	private FloatMap localizedFloatMap;


	// TESTS
	@Test public void testSizeRandomized()
	{
		Random random = new Random(0xdff9d8);

		for(int i = 0; i < 30; i++)
		{
			initFloatMaps(1+random.nextInt(50), 1+random.nextInt(100));
			assertFloatMapsEqual();
		}
	}

	@Test public void testSetPredefined()
	{
		initFloatMaps(50, 50);

		setValueAt(3, 3, 0.2f);
		setValueAt(8, 9, 0.8f);
		setValueAt(0, 3, 0.8f);

		assertFloatMapsEqual();
	}

	@Test public void testSetPredefinedCorners()
	{
		initFloatMaps(100, 100);

		setValueAt(0, 0, 0.2f);
		setValueAt(99, 0, 0.8f);
		setValueAt(99, 99, 0.8f);
		setValueAt(0, 99, 0.8f);

		assertFloatMapsEqual();
	}

	@Test public void testSetRandomized()
	{
		testRandomized(10, 20, 40, 40);
		testRandomized(10, 100, 50, 50);
		testRandomized(5, 5, 300, 300);
	}

	private void testRandomized(int repetitons, int callsToSet, int maxWidth, int maxHeight)
	{
		Random random = new Random(0x543f642);

		for(int i = 0; i < repetitons; i++)
		{
			initFloatMaps(1+random.nextInt(maxWidth), 1+random.nextInt(maxHeight));

			for(int j = 0; j < callsToSet; j++)
				setValueAt(random.nextInt(normalFloatMap.getWidth()), random.nextInt(normalFloatMap.getHeight()),
						random.nextFloat());

			assertFloatMapsEqual();
		}
	}


	// ARRANGE
	private void initFloatMaps(int width, int height)
	{
		normalFloatMap = new FloatMapInMemoryArray(width, height);
		localizedFloatMap = new FloatMapLocalized(width, height);
	}


	// ACT
	private void setValueAt(int x, int y, float value)
	{
		normalFloatMap.set(x, y, value);
		localizedFloatMap.set(x, y, value);
	}


	// ASSERT
	private void assertFloatMapsEqual()
	{
		Assertions.assertEquals(normalFloatMap.getWidth(), localizedFloatMap.getWidth(), "width not identical");
		Assertions.assertEquals(normalFloatMap.getHeight(), localizedFloatMap.getHeight(), "height not identical");

		for(int y = 0; y < normalFloatMap.getHeight(); y++)
			for(int x = 0; x < normalFloatMap.getWidth(); x++)
				Assertions.assertEquals(normalFloatMap.get(x, y), localizedFloatMap.get(x, y), "discrepancy at x"+x+" y"+y);
	}

}
