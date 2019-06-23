package de.domisum.exziff.generator.bedrockregionspartition;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;

public interface BedrockRegionsPartitionGenerator
{

	ShortMap generate(BooleanMap continentsShape, long seed);

}
