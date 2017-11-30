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

	private Map<Material, double[]> materialValuesAtHeight = new HashMap<>();

	// OUTPUT
	private BlockStack mergedBlockStack;


	// MERGE
	public BlockStack merge()
	{
		if(valuedBlockStacks.size() == 1)
			return valuedBlockStacks.get(0).blockStack;

		determineMaterials();
		determineMaxHeight();

		determineMaterialValuesAtHeight();
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

		System.out.println("mater"+valuedBlockStacks.size());
		System.out.println(maxHeight);
	}


	private void determineMaterialValuesAtHeight()
	{
		for(ValuedBlockStack vbs : valuedBlockStacks)
			for(int y = 0; y <= maxHeight; y++)
			{
				// TODO improve this
				Material material = vbs.blockStack.getMaterialAt(y);
				addMaterialValueAtHeight(material, y, vbs.value);
			}
	}

	private void addMaterialValueAtHeight(Material material, int y, double value)
	{
		if(!materialValuesAtHeight.containsKey(material))
			materialValuesAtHeight.put(material, new double[maxHeight+1]);

		double[] valuesAtHeight = materialValuesAtHeight.get(material);
		valuesAtHeight[y] = valuesAtHeight[y]+value;
	}

	private void determineMergedBlockStack()
	{
		Material[] blockStackMaterials = new Material[maxHeight+1];
		for(int y = 0; y <= maxHeight; y++)
		{
			blockStackMaterials[y] = getMergedMaterialAtHeight(y);
			System.out.println("y: "+y+" "+blockStackMaterials[y]);
		}

		mergedBlockStack = BlockStack.fromMaterialArray(blockStackMaterials);
	}

	private Material getMergedMaterialAtHeight(int y)
	{
		Material highestValueMaterial = null;
		double highestValue = -1;
		for(Map.Entry<Material, double[]> entry : materialValuesAtHeight.entrySet())
		{
			double value = entry.getValue()[y];
			if(value > highestValue)
			{
				highestValueMaterial = entry.getKey();
				highestValue = value;
			}
		}

		if(highestValueMaterial == null)
			throw new IllegalStateException("Can't have no material");

		return highestValueMaterial;
	}


	// VALUED BLOCK STACK
	@RequiredArgsConstructor
	public static class ValuedBlockStack
	{

		private final BlockStack blockStack;
		private final double value;

	}

}
