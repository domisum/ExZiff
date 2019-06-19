package de.domisum.exziff.world;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

public class ChunkClusterField
{

	// SETTINGS
	private final int radius;

	// DATA
	private final ChunkCluster[][] clusterField;
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	@Getter
	private final List<ChunkCluster> clusterList = new ArrayList<>();
	private final long[][] lastUsages;


	// INIT
	public ChunkClusterField(int radius)
	{
		Validate.isTrue(radius > 0, "The radius of the chunkClusterField has to be positive");
		this.radius = radius;

		clusterField = new ChunkCluster[radius*2][radius*2];
		lastUsages = new long[radius*2][radius*2];
	}


	// GETTERS
	public ChunkCluster getCluster(int clX, int clZ)
	{
		int arrClX = clX+radius;
		int arrClZ = clZ+radius;

		lastUsages[arrClZ][arrClX] = System.currentTimeMillis();

		return clusterField[arrClZ][arrClX];
	}

	public ChunkCluster getLongestUnusedChunkCluster()
	{
		long longestMsSinceLastUsage = -1;
		int longestUnusedArrX = -1;
		int longestUnusedArrZ = -1;

		long currentTimeMs = System.currentTimeMillis();
		for(int arrZ = 0; arrZ < (radius*2); arrZ++)
			for(int arrX = 0; arrX < (radius*2); arrX++)
			{
				long lastUsage = lastUsages[arrZ][arrX];
				if(lastUsage == 0) // if last usage is 0, the cluster is not loaded
					continue;

				long msSinceLastUsage = currentTimeMs-lastUsage;
				if(msSinceLastUsage > longestMsSinceLastUsage)
				{
					longestMsSinceLastUsage = msSinceLastUsage;
					longestUnusedArrX = arrX;
					longestUnusedArrZ = arrZ;
				}
			}

		ChunkCluster longestUnusedChunkCluster = clusterField[longestUnusedArrZ][longestUnusedArrX];
		return longestUnusedChunkCluster;
	}

	// CLUSTER
	public void addCluster(ChunkCluster chunkCluster)
	{
		int arrClX = chunkCluster.getClX()+radius;
		int arrClZ = chunkCluster.getClZ()+radius;

		clusterField[arrClZ][arrClX] = chunkCluster;
		clusterList.add(chunkCluster);
		lastUsages[arrClZ][arrClX] = System.currentTimeMillis();
	}

	public void removeCluster(ChunkCluster chunkCluster)
	{
		int arrClX = chunkCluster.getClX()+radius;
		int arrClZ = chunkCluster.getClZ()+radius;

		clusterField[arrClZ][arrClX] = null;
		clusterList.remove(chunkCluster);
		lastUsages[arrClZ][arrClX] = 0;
	}

}
