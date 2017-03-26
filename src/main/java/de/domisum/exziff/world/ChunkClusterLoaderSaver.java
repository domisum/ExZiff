package de.domisum.exziff.world;

import de.domisum.exziff.world.transcode.ChunkClusterTranscoder;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ChunkClusterLoaderSaver
{

	// SETTINGS
	@Getter private final boolean saveClusters;
	@Getter private final File chunkClusterDirectory;

	// REFERENCES
	private ChunkClusterTranscoder chunkClusterTranscoder = new ChunkClusterTranscoder();


	// INIT
	public ChunkClusterLoaderSaver(boolean saveClusters, File chunkClusterDirectory)
	{
		this.saveClusters = saveClusters;
		this.chunkClusterDirectory = chunkClusterDirectory;
	}


	// LOADING
	public ChunkCluster loadChunkCluster(int clX, int clZ)
	{
		File clusterFile = new File(this.chunkClusterDirectory, getChunkClusterFileName(clX, clZ));

		if(!clusterFile.exists()) // if the file does not exist, it could not be loaded
			return null;

		// read from file
		byte[] chunkClusterData;
		try
		{
			chunkClusterData = Files.readAllBytes(clusterFile.toPath());
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}

		ChunkCluster decodedChunkCluster = this.chunkClusterTranscoder.decode(chunkClusterData);
		return decodedChunkCluster;
	}


	// SAVING
	public void saveChunkCluster(ChunkCluster chunkCluster)
	{
		if(!this.saveClusters)
			return;

		File clusterFile = new File(this.chunkClusterDirectory,
				getChunkClusterFileName(chunkCluster.getClX(), chunkCluster.getClZ()));

		byte[] encodedChunkCluster = this.chunkClusterTranscoder.encode(chunkCluster);

		try
		{
			Files.write(clusterFile.toPath(), encodedChunkCluster);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	// FILENAME
	private String getChunkClusterFileName(int clX, int clZ)
	{
		String coordinateFormat = "%03d";

		String name = "x"+String.format(coordinateFormat, clX)+"-z"+String.format(coordinateFormat, clZ)+".chnkClstr";
		return name;
	}

}
