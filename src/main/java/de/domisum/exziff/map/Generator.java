package de.domisum.exziff.map;

public interface Generator<I, O>
{

	O generate(I input);

}
