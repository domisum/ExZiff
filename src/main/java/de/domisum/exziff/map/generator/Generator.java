package de.domisum.exziff.map.generator;

public interface Generator<I, O>
{

	O generate(I input);

}
