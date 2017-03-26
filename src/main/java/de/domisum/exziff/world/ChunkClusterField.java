package de.domisum.exziff.world;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

public class ChunkClusterField
{

	// SETTINGS
	private final int radius;

	// DATA
	private final ChunkCluster[][] clusters;
	@Getter private int numberOfClusters = 0;
	private final long[][] lastUsages;


	// INIT
	public ChunkClusterField(int radius)
	{
		Validate.isTrue(radius > 0, "The radius of the chunkClusterField has to be positive");
		this.radius = radius;

		this.clusters = new ChunkCluster[radius*2][radius*2];
		this.lastUsages = new long[radius*2][radius*2];
	}


	// GETTERS
	public ChunkCluster getCluster(int clX, int clZ, boolean updateUsage)
	{
		int arrClX = clX-this.radius;
		int arrClZ = clZ-this.radius;

		if(updateUsage)
			this.lastUsages[arrClZ][arrClX] = System.currentTimeMillis();

		return this.clusters[arrClZ][arrClX];
	}

	public ChunkCluster getLongestUnusedChunkCluster()
	{
		long longestMsSinceLastUsage = Long.MAX_VALUE;
		int longestUnusedArrX = -1;
		int longestUnusedArrZ = -1;

		long currentTimeMs = System.currentTimeMillis();
		for(int arrZ = 0; arrZ < this.radius*2; arrZ++)
			for(int arrX = 0; arrX < this.radius*2; arrX++)
			{
				long lastUsage = this.lastUsages[arrZ][arrX];
				if(lastUsage == 0) // if last usage is null, the cluster is not loaded
					continue;

				long msSinceLastUsage = currentTimeMs-lastUsage;
				if(msSinceLastUsage < longestMsSinceLastUsage)
				{
					longestMsSinceLastUsage = msSinceLastUsage;
					longestUnusedArrX = arrX;
					longestUnusedArrZ = arrZ;
				}
			}

		int clX = longestUnusedArrX+this.radius;
		int clZ = longestUnusedArrZ+this.radius;

		ChunkCluster longestUnusedChunkCluster = getCluster(clX, clZ, false);
		return longestUnusedChunkCluster;
	}


	// CLUSTER
	public void addCluster(ChunkCluster chunkCluster)
	{
		int arrClX = chunkCluster.getClX()-this.radius;
		int arrClZ = chunkCluster.getClZ()-this.radius;

		this.clusters[arrClZ][arrClX] = chunkCluster;
		this.lastUsages[arrClZ][arrClX] = System.currentTimeMillis();
	}

	public void removeCluster(ChunkCluster chunkCluster)
	{
		int arrClX = chunkCluster.getClX()-this.radius;
		int arrClZ = chunkCluster.getClZ()-this.radius;

		this.clusters[arrClZ][arrClX] = null;
		this.lastUsages[arrClX][arrClX] = 0;
	}

}
