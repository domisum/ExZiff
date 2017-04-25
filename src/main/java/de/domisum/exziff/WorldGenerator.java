package de.domisum.exziff;

import de.domisum.exziff.foundation.FoundationRegion;
import de.domisum.exziff.foundation.FoundationRegionBeach;
import de.domisum.exziff.foundation.FoundationRegionType;
import de.domisum.exziff.island.shape.IslandShapeGenerator;
import de.domisum.exziff.map.FloatMap;
import de.domisum.exziff.map.MultiFloatMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.exziff.shape.generator.ShapeMapGenerator;
import de.domisum.exziff.world.Material;
import de.domisum.exziff.world.World;
import de.domisum.exziff.world.loadersaver.ChunkClusterLoaderSaver;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.lib.auxilium.data.container.Duo;
import de.domisum.lib.auxilium.data.container.IntBounds2D;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WorldGenerator
{

	// SETTINGS
	private static final int FOUNDATION_REGION_BLEND_DISTANCE = 10;
	private static final int WATER_HEIGHT = 50;

	// INPUT
	private int size;
	private long seed;

	private File worldDirectory;

	// TEMP
	private ShapeMap continentShape;

	private Map<Integer, FoundationRegion> foundationRegions = new HashMap<>();
	private ShortMap foundationRegionsMap;
	private MultiFloatMap foundationRegionInfluenceMap = new MultiFloatMap();

	// OUTPUT
	private World world;


	// INIT
	public WorldGenerator(int size, long seed, File worldDirectory)
	{
		this.size = size;
		this.seed = seed;

		this.worldDirectory = worldDirectory;
	}


	// GENERATION
	public World generate()
	{
		ChunkClusterLoaderSaver chunkClusterLoaderSaver = new ChunkClusterLoaderSaver(this.worldDirectory, true);
		this.world = new World(chunkClusterLoaderSaver);


		// shape
		generateContinentShape();

		// foundation
		generateFoundationRegions();
		blendFoundationRegions();
		System.out.println("before world build");
		buildFoundation();

		this.world.save();
		return this.world;
	}


	// SHAPE
	private void generateContinentShape()
	{
		ShapeMapGenerator generator = new IslandShapeGenerator(this.size, this.size, this.seed*1337L);

		this.continentShape = generator.generate();
	}


	// FOUNDATION
	private void generateFoundationRegions()
	{
		// this is just test code
		this.foundationRegionsMap = new ShortMap(this.size, this.size);

		determineBeachRegions();


		int divisions = this.size/64;
		int divisionSize = this.size/divisions;


		NoiseLayer[] noiseLayers = new NoiseLayer[] {new NoiseLayer(200, 50, this.seed*9239L),
				new NoiseLayer(20, 15, this.seed*92378389749L), new NoiseLayer(10, 7, this.seed*923783L)};

		LayeredOpenSimplexNoise noiseX = new LayeredOpenSimplexNoise(noiseLayers);
		LayeredOpenSimplexNoise noiseZ = new LayeredOpenSimplexNoise(noiseLayers);

		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				if(!this.continentShape.get(x, z))
					continue;

				if(this.foundationRegionsMap.get(x, z) != 0)
					continue;

				double dX = noiseX.evaluate(x, z);
				double dZ = noiseZ.evaluate(x+1000, z+5000);

				int offsetX = (int) Math.round(x+dX);
				int offsetZ = (int) Math.round(z+dZ);
				if(!isInBounds(offsetX, offsetZ))
					continue;

				int regionId = (offsetZ/divisionSize)*divisions+(offsetX/divisionSize)+1;
				this.foundationRegionsMap.set(x, z, (short) regionId);
			}

		this.foundationRegions.put(0, FoundationRegionType.OCEAN_FLOOR.getInstance(0, this.seed*1734913+47893948));
		for(int i = 1; i <= divisions*divisions; i++)
		{
			if(!isRegionUsed(i))
				continue;

			FoundationRegion foundationRegion = getRandomFoundationRegion(i);
			this.foundationRegions.put(i, foundationRegion);
		}

		// test export
		exportFoundationRegions();
	}

	private void blendFoundationRegions()
	{
		// create maps

		// ocean map, use normal FloatMap since it covers the whole map
		this.foundationRegionInfluenceMap.setMap(0, new FloatMap(this.size, this.size));
		for(Map.Entry<Integer, FoundationRegion> entry : this.foundationRegions.entrySet())
		{
			FloatMap influenceMap = new FloatMap(this.size, this.size); // TODO add reduced memory FloatMap
			this.foundationRegionInfluenceMap.setMap(entry.getKey(), influenceMap);
		}

		// add influence to the MultiMap
		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				if(!this.continentShape.get(x, z)) // ocean
				{
					//this.foundationRegionInfluenceMap.set(x, z, 0, 0.01f);
					continue;
				}

				int regionId = this.foundationRegionsMap.get(x, z);

				for(int rZ = -FOUNDATION_REGION_BLEND_DISTANCE; rZ <= FOUNDATION_REGION_BLEND_DISTANCE; rZ++)
					for(int rX = -FOUNDATION_REGION_BLEND_DISTANCE; rX <= FOUNDATION_REGION_BLEND_DISTANCE; rX++)
					{
						int nX = x+rX;
						int nZ = z+rZ;

						if(!isInBounds(nX, nZ))
							continue;

						double distance = Math.sqrt(rX*rX+rZ*rZ);
						if(distance >= FOUNDATION_REGION_BLEND_DISTANCE)
							continue;

						double regionInfluence = 1-(distance/FOUNDATION_REGION_BLEND_DISTANCE);

						float currentRegionInfluence = this.foundationRegionInfluenceMap.get(nX, nZ, regionId);

						// never reduce influence
						if(regionInfluence <= currentRegionInfluence)
							continue;

						this.foundationRegionInfluenceMap.set(nX, nZ, regionId, (float) regionInfluence);
					}
			}

		// add ocean influence
		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				if(this.continentShape.get(x, z))
					continue;

				Set<Duo<Integer, Float>> influences = this.foundationRegionInfluenceMap.get(x, z);

				float influenceSum = 0;
				for(Duo<Integer, Float> influence : influences)
					influenceSum += influence.b;

				if(influences.size() != 0)
					influenceSum /= influences.size();

				float oceanInfluence = 1-influenceSum;
				this.foundationRegionInfluenceMap.set(x, z, 0, oceanInfluence);
			}

		// normalize influence values
		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				Set<Duo<Integer, Float>> influences = this.foundationRegionInfluenceMap.get(x, z);

				float influenceSum = 0;
				for(Duo<Integer, Float> influence : influences)
					influenceSum += influence.b;

				float normalizationFactor = 1/influenceSum;

				for(Duo<Integer, Float> influence : influences)
					this.foundationRegionInfluenceMap.set(x, z, influence.a, influence.b*normalizationFactor);
			}
	}

	private void buildFoundation()
	{
		// generate foundationRegions
		for(Map.Entry<Integer, FoundationRegion> entry : this.foundationRegions.entrySet())
		{
			FoundationRegion foundationRegion = entry.getValue();

			FloatMap influenceMap = this.foundationRegionInfluenceMap.getMap(entry.getKey());
			foundationRegion.setInfluenceMap(influenceMap);
			/*IntBounds2D influenceMapBounds = determineFloatMapBounds(influenceMap);

			influenceMapBounds = influenceMapBounds.expand(10);
			influenceMapBounds = influenceMapBounds.limit(new IntBounds2D(0, this.size, 0, this.size));

			foundationRegion.setBounds(influenceMapBounds);*/
			foundationRegion.generate();
		}

		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				Set<Duo<Integer, Float>> influences = this.foundationRegionInfluenceMap.get(x, z);

				double sumHeight = 0;
				for(Duo<Integer, Float> duo : influences)
				{
					FoundationRegion foundationRegion = this.foundationRegions.get(duo.a);

					sumHeight += foundationRegion.getHeightAt(x, z)*duo.b;
				}

				int combinedHeight = (int) Math.round(sumHeight);


				// land
				for(int y = 0; y <= combinedHeight; y++)
					this.world.setMaterial(x, y, z, Material.STONE);

				// water
				if(!this.continentShape.get(x, z))// && (x%8 == 0 && z%8 == 0))
					for(int y = combinedHeight+1; y <= WATER_HEIGHT; y++)
						this.world.setMaterial(x, y, z, Material.WATER);
			}
	}


	// helper
	private boolean isInBounds(int x, int z)
	{
		return x >= 0 && z >= 0 && x < this.size && z < this.size;
	}

	@SuppressWarnings("ConstantConditions") private IntBounds2D determineFloatMapBounds(FloatMap floatMap)
	{
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxZ = Integer.MIN_VALUE;


		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				float value = floatMap.get(x, z);
				if(value == 0)
					continue;

				if(x < minX)
					minX = x;
				if(x > maxX)
					maxX = x;

				if(z < minZ)
					minZ = z;
				if(z > maxZ)
					maxZ = z;
			}

		// float map is empty
		if(minX == Integer.MAX_VALUE)
			return null;

		return new IntBounds2D(minX, maxX, minZ, maxZ);
	}


	// test
	private void determineBeachRegions()
	{
		int beachRegionId = Short.MAX_VALUE-29;
		this.foundationRegions.put(beachRegionId, new FoundationRegionBeach(beachRegionId, this.seed+394939439L));

		int beachRadius = 15;

		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				if(this.continentShape.get(x, z))
					continue;

				for(int rZ = -beachRadius; rZ <= beachRadius; rZ++)
					for(int rX = -beachRadius; rX <= beachRadius; rX++)
					{
						int oX = x+rX;
						int oZ = z+rZ;

						if(!isInBounds(oX, oZ))
							continue;

						if(!this.continentShape.get(oX, oZ))
							continue;

						double distance = Math.sqrt(rX*rX+rZ*rZ);
						if(distance > beachRadius)
							continue;

						this.foundationRegionsMap.set(oX, oZ, (short) beachRegionId);
					}
			}
	}

	private FoundationRegion getRandomFoundationRegion(int id)
	{
		Random random = new Random((this.seed+id)*120014384L);
		long foundationRegionSeed = random.nextLong();

		FoundationRegionType type;
		do
			type = RandomUtil.getElement(FoundationRegionType.values(), random);
		while(type == FoundationRegionType.OCEAN_FLOOR || type == FoundationRegionType.BEACH);

		return type.getInstance(id, foundationRegionSeed);
	}

	private void exportFoundationRegions()
	{
		System.out.println("export");

		int minBrightness = 50;

		int[][] pixels = new int[this.size][this.size];

		for(int y = 0; y < this.size; y++)
			for(int x = 0; x < this.size; x++)
			{
				if(!this.continentShape.get(x, y))
					continue;

				int regionId = this.foundationRegionsMap.get(x, y);
				FoundationRegion region = this.foundationRegions.get(regionId);

				int brightnessInt = (regionId*67)%(256-minBrightness)+minBrightness;
				int color = (brightnessInt)|(brightnessInt<<8)|(brightnessInt<<16);

				if(region.getType() == FoundationRegionType.FLATLANDS)
					color = (10<<16)|(250<<8)|((27*regionId)%100);
				else if(region.getType() == FoundationRegionType.PLATEAU_HILLS)
					color = (250<<16)|(10<<8)|((27*regionId)%100);
				else if(region.getType() == FoundationRegionType.BEACH)
					color = (250<<16)|(250<<8)|((27*regionId)%100);

				pixels[y][x] = color;
			}

		BufferedImage image = ImageUtil.getImageFromPixels(pixels);
		ImageUtil.writeImage(new File("C:/Users/domisum/testChamber/testWorld.png"), image);
	}

	private boolean isRegionUsed(int regionId)
	{
		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
				if(this.foundationRegionsMap.get(x, z) == regionId)
					return true;

		return false;
	}

}
