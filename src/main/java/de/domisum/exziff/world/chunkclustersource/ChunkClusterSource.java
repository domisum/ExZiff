package de.domisum.exziff.world.chunkclustersource;

import de.domisum.exziff.world.ChunkCluster;

public interface ChunkClusterSource
{

	ChunkCluster loadChunkCluster(int clX, int clZ);

	void saveChunkCluster(ChunkCluster chunkCluster);

}