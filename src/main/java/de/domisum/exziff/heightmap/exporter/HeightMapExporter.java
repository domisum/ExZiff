package de.domisum.exziff.heightmap.exporter;

import de.domisum.exziff.heightmap.HeightMap;

public abstract class HeightMapExporter<T>
{

	// -------
	// EXPORT
	// -------
	public abstract T export(HeightMap input);

}
