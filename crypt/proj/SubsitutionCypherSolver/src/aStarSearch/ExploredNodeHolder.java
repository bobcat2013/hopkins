package aStarSearch;

import java.util.ArrayList;

public class ExploredNodeHolder {
protected ArrayList<AStarNode> seenNodes;
private double bestScore;

public ExploredNodeHolder()
{
	seenNodes = new ArrayList<AStarNode>();
	bestScore = Double.MAX_VALUE;
}

public boolean isEmpty()
{
	return seenNodes.size() == 0;
}
public void addNewNode(AStarNode newNode)
{
	double hCost =newNode.gethCost();
	if(this.seenNodes.size() == 0)
	{
		this.seenNodes.add(newNode);
		bestScore = hCost;
		return;
	}
	
	for(int ii = 0; ii< this.seenNodes.size(); ii++)
	{
		if(newNode.gethCost()<seenNodes.get(ii).gethCost())
		{
			seenNodes.add(ii,newNode);
			break;
		}
	}
	if(seenNodes.size()>0)
		bestScore = seenNodes.get(0).gethCost();
	return;
}
public double getbestScore()
{
	if(seenNodes.size()>0)
		bestScore = seenNodes.get(0).gethCost();
	return bestScore;
}
public AStarNode visitBestNode()
{
	AStarNode bestNode  = seenNodes.get(0);
	seenNodes.remove(0);
	ArrayList<AStarNode> children =  bestNode.getChildren();
	for(AStarNode child :children)
	{
		this.addNewNode(child);
	}
	
	return bestNode;
}

}
