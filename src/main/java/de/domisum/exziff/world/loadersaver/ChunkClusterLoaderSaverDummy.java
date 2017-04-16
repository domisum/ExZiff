package de.domisum.exziff.world.loadersaver;

import de.domisum.exziff.world.ChunkCluster;

import java.io.File;

public class ChunkClusterLoaderSaverDummy extends ChunkClusterLoaderSaver
{

	// INIT
	public ChunkClusterLoaderSaverDummy(File chunkClusterDirectory, boolean saveClusters)
	{
		super(chunkClusterDirectory, saveClusters);
	}


	// DUMMY
	@Override public boolean isSaveClusters()
	{
		return false;
	}

	@Override public File getChunkClusterDirectory()
	{
		return null;
	}

	@Override public ChunkCluster loadChunkCluster(int clX, int clZ)
	{
		return null;
	}

	@Override public void saveChunkCluster(ChunkCluster chunkCluster)
	{

	}

}
