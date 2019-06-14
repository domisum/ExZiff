package de.domisum.exziff.oldworld.transcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TranscoderTest
{

	@Test public void testEncodeDecodeIntPredefined()
	{
		assertEncodeDecodeIntEquals(5, 0);
		assertEncodeDecodeIntEquals(5, 1);
		assertEncodeDecodeIntEquals(3, -1);
		assertEncodeDecodeIntEquals(0, 17);
		assertEncodeDecodeIntEquals(88, -234234343);
	}

	@Test public void testEncodeDecodeIntRandomized()
	{
		Random random = new Random(0xaffe);

		for(int test = 0; test < 1000; test++)
			assertEncodeDecodeIntEquals(random.nextInt(500), random.nextInt());
	}


	private void assertEncodeDecodeIntEquals(int startingPosition, int number)
	{
		byte[] bytes = new byte[startingPosition+16];

		Transcoder.encodeInt(bytes, startingPosition, number);
		int decoded = Transcoder.decodeInt(bytes, startingPosition);
		Assertions.assertEquals(number, decoded);
	}

}
