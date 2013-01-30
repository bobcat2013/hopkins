package aStarSearch;

import java.util.ArrayList;

public abstract class AStarNode {
	private double hCost;
	private int stepsToReach;
	private ArrayList<AStarNode> children;
	private boolean amIRoot;
	private boolean isLeaf = false;
	private AStarNode parent;
	
	public AStarNode(int steps)
	{
		amIRoot = (steps==0);
		stepsToReach = steps;
	}
	
	public AStarNode(int steps, AStarNode givenP)
	{
		amIRoot = (steps==0);
		stepsToReach = steps;
		parent = givenP;
	}
	
	public AStarNode getParent()
	{
		return parent;
	}
	public double gethCost() {
		return hCost;
	}


	public int getDepth() {
		return stepsToReach;
	}


	public ArrayList<AStarNode> getChildren() {
		return children;
	}


	public boolean isRoot() {
		return amIRoot;
	}


	public boolean isLeaf() {
		return isLeaf;
	}


	public void makeChild()
	{}
	
}
