package de.domisum.exziff.generator;

public interface RandomizedGenerator<I, O>
{

	O generate(long seed, I input);

}
