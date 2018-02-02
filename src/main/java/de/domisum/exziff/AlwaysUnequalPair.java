package de.domisum.exziff;

import de.domisum.lib.auxilium.data.container.tuple.Pair;

public class AlwaysUnequalPair<T, U> extends Pair<T, U>
{

	// INIT
	public AlwaysUnequalPair(T a, U b)
	{
		super(a, b);
	}


	@Override public boolean equals(Object o)
	{
		return false;
	}

}