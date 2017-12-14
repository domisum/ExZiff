package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.world.Material;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class BlockStackMerger
{

	// INPUT
	private final List<ValuedBlockStack> valuedBlockStacks;

	// TEMP
	private Set<Material> materials = new HashSet<>();
	private int maxHeight = -1;

	private Map<ValuedBlockStack, Map<Material, int[]>> toMaterialsDistancesAtHeight = new HashMap<>();
	private Map<Material, double[]> materialRepulsionAtHeight = new HashMap<>();

	// OUTPUT
	private BlockStack mergedBlockStack;


	// MERGE
	public BlockStack merge()
	{
		if(valuedBlockStacks.size() == 1)
			return valuedBlockStacks.get(0).blockStack;

		determineMaterials();
		determineMaxHeight();

		determineMaterialRepulsionAtHeight();
		determineMergedBlockStack();
		return mergedBlockStack;
	}

	private void determineMaterials()
	{
		materials.add(Material.AIR);

		for(ValuedBlockStack vbs : valuedBlockStacks)
			for(int y = 0; y <= vbs.blockStack.getMaximumY(); y++)
			{
				Material material = vbs.blockStack.getMaterialAt(y);
				if(!materials.contains(material))
					materials.add(material);
			}
	}

	private void determineMaxHeight()
	{
		for(ValuedBlockStack vbs : valuedBlockStacks)
			if(vbs.blockStack.getMaximumY() > maxHeight)
				maxHeight = vbs.blockStack.getMaximumY();
	}


	private void determineMaterialRepulsionAtHeight()
	{
		determineMaterialDistances();

		for(ValuedBlockStack vbs : valuedBlockStacks)
			for(Material m : materials)
				for(int y = 0; y <= maxHeight; y++)
				{
					double toMaterialDistance = toMaterialsDistancesAtHeight.get(vbs).get(m)[y];
					double repulsion = toMaterialDistance*vbs.value;

					addMaterialRepulsionAtHeight(m, y, repulsion);
				}
	}

	private void determineMaterialDistances()
	{
		for(ValuedBlockStack vbs : valuedBlockStacks)
		{
			Map<Material, int[]> toMaterialsDistancesAtHeight = new HashMap<>();
			this.toMaterialsDistancesAtHeight.put(vbs, toMaterialsDistancesAtHeight);

			for(Material m : materials)
			{
				int[] toMaterialDistancesAtHeight = determineDistancesAtHeight(vbs.blockStack, m);
				toMaterialsDistancesAtHeight.put(m, toMaterialDistancesAtHeight);
			}
		}
	}

	private int[] determineDistancesAtHeight(BlockStack blockStack, Material material)
	{
		int startDistance = 50;


		int[] distancesAtHeight = new int[maxHeight+1];

		int distance = startDistance;
		for(int y = 0; y <= maxHeight; y++)
		{
			if(blockStack.getMaterialAt(y) == material)
				distance = 0;

			distancesAtHeight[y] = distance;
			distance++;
		}

		distance = startDistance;
		for(int y = maxHeight; y >= 0; y--)
		{
			if(blockStack.getMaterialAt(y) == material)
				distance = 0;

			if(distancesAtHeight[y] > distance)
				distancesAtHeight[y] = distance;
			distance++;
		}

		return distancesAtHeight;
	}


	private void addMaterialRepulsionAtHeight(Material material, int y, double value)
	{
		if(!materialRepulsionAtHeight.containsKey(material))
			materialRepulsionAtHeight.put(material, new double[maxHeight+1]);

		double[] valuesAtHeight = materialRepulsionAtHeight.get(material);
		valuesAtHeight[y] = valuesAtHeight[y]+value;
	}


	private void determineMergedBlockStack()
	{
		Material[] blockStackMaterials = new Material[maxHeight+1];
		for(int y = 0; y <= maxHeight; y++)
			blockStackMaterials[y] = getMergedMaterialAtHeight(y);

		mergedBlockStack = BlockStack.fromMaterialArray(blockStackMaterials);
	}

	private Material getMergedMaterialAtHeight(int y)
	{
		Material lowestRepulsionMaterial = null;
		double lowestRepulsion = Double.MAX_VALUE;
		for(Map.Entry<Material, double[]> entry : materialRepulsionAtHeight.entrySet())
		{
			double value = entry.getValue()[y];
			if(value < lowestRepulsion)
			{
				lowestRepulsionMaterial = entry.getKey();
				lowestRepulsion = value;
			}
		}

		if(lowestRepulsionMaterial == null)
			throw new IllegalStateException("Can't have no material");

		return lowestRepulsionMaterial;
	}


	// VALUED BLOCK STACK
	@RequiredArgsConstructor
	public static class ValuedBlockStack
	{

		private final BlockStack blockStack;
		private final double value;

	}

}
