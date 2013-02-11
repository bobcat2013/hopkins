package aStarSearch;

import java.util.ArrayList;

public abstract class AStarNode {
	protected double hCost;
	private int stepsToReach;
	protected ArrayList<AStarNode> children;
	private boolean amIRoot;
	private boolean isLeaf = false;
	private boolean isScored;
	private AStarNode parent;
	
	public AStarNode(int steps)
	{
		amIRoot = (steps==0);
		stepsToReach = steps;
		isScored = false;
		children = new ArrayList<AStarNode>();
	}
	
	public AStarNode(int steps, AStarNode givenP)
	{
		amIRoot = (steps==0);
		stepsToReach = steps;
		parent = givenP;
		isScored = false;
		children = new ArrayList<AStarNode>();
	}
	public boolean scored()
	{
		return this.isScored;
	}
	
	public void setCost(double in)
	{
		this.isScored=true;
		this.hCost=in;
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


	public void makeChildren()
	{}
	
}
