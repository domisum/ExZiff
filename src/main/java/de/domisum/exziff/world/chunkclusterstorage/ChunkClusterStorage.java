package de.domisum.exziff.world.chunkclusterstorage;

import de.domisum.exziff.world.ChunkCluster;

public interface ChunkClusterStorage
{

	ChunkCluster loadChunkCluster(int clX, int clZ);

	void saveChunkCluster(ChunkCluster chunkCluster);

}
