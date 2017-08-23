package de.domisum.exziff.shape.continent;

import de.domisum.exziff.map.BooleanMap;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class ContinentShapeNoiseDeformer
{

	// SETTINGS
	@Getter @Setter private int iterations;

	// REFERENCES
	private Random random;

	// TEMP
	private BooleanMap map;


	// INIT
	public ContinentShapeNoiseDeformer(long seed)
	{
		this.random = new Random(seed);
	}


	// DEFORMING
	public synchronized BooleanMap deform(BooleanMap input)
	{
		this.map = input;

		for(int i = 0; i < this.iterations; i++)
		{
			scatter();
			cleanup();
		}

		return this.map;
	}

	private void scatter()
	{

	}

	private void cleanup()
	{

	}

}
