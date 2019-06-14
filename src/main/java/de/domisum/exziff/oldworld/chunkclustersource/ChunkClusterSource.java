package de.domisum.exziff.oldworld.chunkclustersource;

import de.domisum.exziff.oldworld.ChunkCluster;

public interface ChunkClusterSource
{

	ChunkCluster loadChunkCluster(int clX, int clZ);

	void saveChunkCluster(ChunkCluster chunkCluster);

}
