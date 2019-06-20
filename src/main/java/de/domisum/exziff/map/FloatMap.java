package de.domisum.exziff.map;

public interface FloatMap
{
	// GETTERS
	int getWidth();

	int getHeight();

	float get(int x, int y);

	// SETTERS
	void set(int x, int y, float value);

}
