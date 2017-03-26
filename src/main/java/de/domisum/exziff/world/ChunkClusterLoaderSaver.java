package de.domisum.exziff.world;

import de.domisum.exziff.world.transcode.ChunkClusterTranscoder;
import de.domisum.lib.auxilium.util.CompressionUtil;
import de.domisum.lib.auxilium.util.FileUtil;
import lombok.Getter;

import java.io.File;

public class ChunkClusterLoaderSaver
{

	// SETTINGS
	@Getter private final boolean saveClusters;
	@Getter private final File chunkClusterDirectory;

	// REFERENCES
	private ChunkClusterTranscoder chunkClusterTranscoder = new ChunkClusterTranscoder();


	// INIT
	public ChunkClusterLoaderSaver(File chunkClusterDirectory, boolean saveClusters)
	{
		this.chunkClusterDirectory = chunkClusterDirectory;
		this.saveClusters = saveClusters;

		if(!chunkClusterDirectory.exists())
			chunkClusterDirectory.mkdirs();
	}


	// LOADING
	public ChunkCluster loadChunkCluster(int clX, int clZ)
	{
		File clusterFile = new File(this.chunkClusterDirectory, getChunkClusterFileName(clX, clZ));

		if(!clusterFile.exists()) // if the file does not exist, it could not be loaded
			return null;

		// read from file
		byte[] chunkClusterData = FileUtil.readFileToByteArray(clusterFile);
		chunkClusterData = CompressionUtil.decompress(chunkClusterData);

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
		encodedChunkCluster = CompressionUtil.compress(encodedChunkCluster, false);

		FileUtil.writeByteArrayToFile(clusterFile, encodedChunkCluster);
	}


	// FILENAME
	private String getChunkClusterFileName(int clX, int clZ)
	{
		String coordinateFormat = "%03d";

		String name = "x"+String.format(coordinateFormat, clX)+"-z"+String.format(coordinateFormat, clZ)+".chnkClstr";
		return name;
	}

}
