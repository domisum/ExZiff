package de.domisum.exziff.world.transcode;

import de.domisum.lib.auxilium.util.math.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TranscoderTest
{

	@Test
	public void testEncodeDecodeIntPredefined()
	{
		assertEncodeDecodeIntEquals(5, 0);
		assertEncodeDecodeIntEquals(5, 1);
		assertEncodeDecodeIntEquals(3, -1);
		assertEncodeDecodeIntEquals(0, 17);
		assertEncodeDecodeIntEquals(88, -234234343);
	}

	@Test
	public void testEncodeDecodeIntRandomized()
	{
		Random random = new Random(0xaffe);

		for(int test = 0; test < 1000; test++)
			assertEncodeDecodeIntEquals(random.nextInt(500), random.nextInt());
	}

	@Test
	public void testEncodeDecodeShortPredefined()
	{
		assertEncodeDecodeIntEquals(5, 0);
		assertEncodeDecodeIntEquals(5, 1);
		assertEncodeDecodeIntEquals(3, -1);
		assertEncodeDecodeIntEquals(0, 17);
		assertEncodeDecodeIntEquals(27, Short.MAX_VALUE);
		assertEncodeDecodeIntEquals(88, Short.MIN_VALUE);
	}

	@Test
	public void testEncodeDecodeShortRandomized()
	{
		Random random = new Random(0xffaff);

		for(int test = 0; test < 1000; test++)
			assertEncodeDecodeShortEquals(random.nextInt(500), RandomUtil.nextShort(random));
	}


	private void assertEncodeDecodeIntEquals(int startingPosition, int number)
	{
		byte[] bytes = new byte[startingPosition+100];

		Transcoder.encodeInt(bytes, startingPosition, number);
		int decoded = Transcoder.decodeInt(bytes, startingPosition);
		Assertions.assertEquals(number, decoded);
	}

	private void assertEncodeDecodeShortEquals(int startingPosition, short number)
	{
		byte[] bytes = new byte[startingPosition+100];

		Transcoder.encodeShort(bytes, startingPosition, number);
		short decoded = Transcoder.decodeShort(bytes, startingPosition);
		Assertions.assertEquals(number, decoded);
	}

}
