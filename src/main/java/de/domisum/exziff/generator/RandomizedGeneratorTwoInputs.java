package de.domisum.exziff.generator;

public interface RandomizedGeneratorTwoInputs<I1, I2, O>
{

	O generate(long seed, I1 input1, I2 input2);

}
