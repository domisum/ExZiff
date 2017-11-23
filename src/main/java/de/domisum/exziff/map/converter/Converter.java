package de.domisum.exziff.map.converter;

public interface Converter<I, O>
{

	O convert(I input);

}
