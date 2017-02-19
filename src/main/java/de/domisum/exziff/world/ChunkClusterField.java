package de.domisum.exziff.world;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.Validate;

public class ChunkClusterField
{

	// SETTINGS
	private final int radius;

	// DATA
	private final ChunkCluster[][] clusters;


	// -------
	// INIT
	// -------
	public ChunkClusterField(int radius)
	{
		Validate.isTrue(radius > 0, "The radius of the chunkClusterField has to be positive");
		this.radius = radius;

		this.clusters = new ChunkCluster[radius*2][radius*2];
	}


	// -------
	// GETTERS
	// -------
	@Nullable public ChunkCluster getChunkCluster(int clX, int clZ)
	{
		int arrClX = clX-this.radius;
		int arrClZ = clZ-this.radius;

		return this.clusters[arrClZ][arrClX];
	}

}
