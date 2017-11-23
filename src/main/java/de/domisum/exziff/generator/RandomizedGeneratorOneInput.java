package de.domisum.exziff.generator;

public interface RandomizedGeneratorOneInput<I, O>
{

	O generate(long seed, I input);

}
