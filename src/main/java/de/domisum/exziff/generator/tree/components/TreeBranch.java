package de.domisum.exziff.generator.tree.components;

import de.domisum.exziff.blocksource.BlockSource;
import de.domisum.exziff.blockstructure.BlockStructure;
import de.domisum.exziff.world.block.Block;
import de.domisum.lib.auxilium.data.container.math.Vector3D;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class TreeBranch
{

	private final List<Node> nodes = new ArrayList<>();


	// NODES
	public void addNode(Vector3D position, double radius, BlockSource blockSource)
	{
		nodes.add(new Node(position, radius, blockSource));
	}


	// RENDER
	public BlockStructure render()
	{
		BlockStructure blockStructure = new BlockStructure();

		for(Node node : nodes)
			renderNode(node, blockStructure);

		return blockStructure;
	}

	private void renderNode(Node node, BlockStructure blockStructure)
	{
		double radiusCeil = Math.ceil(node.radius);

		for(double dX = -radiusCeil; dX <= radiusCeil; dX++)
			for(double dY = -radiusCeil; dY <= radiusCeil; dY++)
				for(double dZ = -radiusCeil; dZ <= radiusCeil; dZ++)
				{
					Vector3D dPosition = node.position.add(dX, dY, dZ);
					if(dPosition.distanceTo(node.position) > node.radius)
						continue;

					int dPosX = (int) Math.floor(dPosition.getX());
					int dPosY = (int) Math.floor(dPosition.getY());
					int dPosZ = (int) Math.floor(dPosition.getZ());

					Block block = node.blockSource.get(dPosX, dPosY, dPosZ);
					if(block == null)
						continue;

					blockStructure.setBlockIfNotSetAlready(dPosX, dPosY, dPosZ, block);
				}

	}


	@RequiredArgsConstructor
	private static class Node
	{

		private final Vector3D position;
		private final double radius;
		private final BlockSource blockSource;

	}

}
