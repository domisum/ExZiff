package de.domisum.exziff.world.transcode;

public interface Transcoder<T>
{

	// TRANSCODING
	byte[] encode(T toEncode);

	T decode(byte[] toDecode);


	// PRIMITIVE TRANSCODING
	default void encodeInt(byte[] encodeTo, int startingPosition, int number)
	{
		encodeTo[startingPosition] = (byte) (number>>24);
		encodeTo[startingPosition+1] = (byte) (number>>16);
		encodeTo[startingPosition+2] = (byte) (number>>8);
		encodeTo[startingPosition+3] = (byte) (number);
	}

	default int decodeInt(byte[] decodeFrom, int startingPosition)
	{
		int number = 0;
		number |= (decodeFrom[startingPosition]&0xFF)<<24;
		number |= (decodeFrom[startingPosition+1]&0xFF)<<16;
		number |= (decodeFrom[startingPosition+2]&0xFF)<<8;
		number |= (decodeFrom[startingPosition+3]&0xFF);

		return number;
	}

}
