package de.domisum.exziff.world.chunkclusterstorage;

import de.domisum.exziff.world.ChunkCluster;
import de.domisum.exziff.world.transcode.ChunkClusterTranscoder;
import de.domisum.lib.auxilium.util.CompressionUtil;
import de.domisum.lib.auxilium.util.CompressionUtil.Speed;
import de.domisum.lib.auxilium.util.FileUtil;
import lombok.Getter;

import java.io.File;

public class ChunkClusterStorageFromDisk implements ChunkClusterStorage
{

	// SETTINGS
	@Getter
	private final boolean savingEnabled;
	@Getter
	private final File chunkClusterDirectory;

	// REFERENCES
	private final ChunkClusterTranscoder chunkClusterTranscoder = new ChunkClusterTranscoder();


	// INIT
	public ChunkClusterStorageFromDisk(File chunkClusterDirectory, boolean savingEnabled)
	{
		this.chunkClusterDirectory = chunkClusterDirectory;
		this.savingEnabled = savingEnabled;

		if(!chunkClusterDirectory.exists())
			chunkClusterDirectory.mkdirs();
	}


	// LOADING
	@Override
	public ChunkCluster loadChunkCluster(int clX, int clZ)
	{
		File clusterFile = new File(chunkClusterDirectory, getChunkClusterFileName(clX, clZ));

		if(!clusterFile.exists()) // if the file does not exist, it could not be loaded
			return null;

		// read from file
		byte[] chunkClusterData = FileUtil.readRaw(clusterFile);
		chunkClusterData = CompressionUtil.decompress(chunkClusterData);

		ChunkCluster decodedChunkCluster = chunkClusterTranscoder.decode(chunkClusterData);
		return decodedChunkCluster;
	}


	// SAVING
	@Override
	public void saveChunkCluster(ChunkCluster chunkCluster)
	{
		if(!savingEnabled)
			return;

		File clusterFile = new File(chunkClusterDirectory, getChunkClusterFileName(chunkCluster.getClX(), chunkCluster.getClZ()));

		byte[] encodedChunkCluster = chunkClusterTranscoder.encode(chunkCluster);
		encodedChunkCluster = CompressionUtil.compress(encodedChunkCluster, Speed.FAST);

		FileUtil.writeRaw(clusterFile, encodedChunkCluster);
	}


	// FILENAME
	private String getChunkClusterFileName(int clX, int clZ)
	{
		String coordinateFormat = "%03d";

		String name = "x"+String.format(coordinateFormat, clX)+"-z"+String.format(coordinateFormat, clZ)+".chnkClstr";
		return name;
	}

}
