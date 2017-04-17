package de.domisum.exziff.world.transcode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class TranscoderTest
{

	@Test public void testEncodeDecodeIntPredefined()
	{
		Transcoder<?> transcoder = getDummyTranscoder();

		encodeDecodeIntAssertEquals(transcoder, 5, 0);
		encodeDecodeIntAssertEquals(transcoder, 3, -1);
		encodeDecodeIntAssertEquals(transcoder, 0, 17);
		encodeDecodeIntAssertEquals(transcoder, 88, -234234343);
	}

	@Test public void testEncodeDecodeIntRandomized()
	{
		Transcoder<?> transcoder = getDummyTranscoder();
		Random random = new Random(0xaffe);

		for(int test = 0; test < 1000; test++)
			encodeDecodeIntAssertEquals(transcoder, random.nextInt(500), random.nextInt());
	}


	private void encodeDecodeIntAssertEquals(Transcoder<?> transcoder, int startingPosition, int number)
	{
		byte[] bytes = new byte[startingPosition+16];

		transcoder.encodeInt(bytes, startingPosition, number);
		int decoded = transcoder.decodeInt(bytes, startingPosition);
		Assert.assertEquals(number, decoded);
	}


	private Transcoder<?> getDummyTranscoder()
	{
		Transcoder<?> transcoder = new Transcoder()
		{
			@Override public byte[] encode(Object toEncode)
			{
				return new byte[0];
			}

			@Override public Object decode(byte[] toDecode)
			{
				return null;
			}
		};

		return transcoder;
	}


}