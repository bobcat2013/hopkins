package cypherSolver;

import java.util.ArrayList;

import aStarSearch.AStarNode;

public class CypherNode extends AStarNode {

	private KeyHolder kh;

	
	public CypherNode(int steps, KeyHolder inKH) {
		super(steps);
		kh = inKH;
	}

	public KeyHolder getKeyHolder()
	{
		return this.kh;
	}
	
	public ArrayList<KeyHolder> getKeyHoldersFromKids()
	{
		ArrayList<KeyHolder> outKeys = new ArrayList<KeyHolder>();
		for(AStarNode childAStarNode : this.children)
			outKeys.add(((CypherNode)childAStarNode).getKeyHolder());
		return outKeys;
	}
	
	public void setChildrenCost(ArrayList<Double> scores)
	{
		for(int ii=0; ii<scores.size(); ii++)
		{
			children.get(ii).setCost(scores.get(ii));
		}
	}
	
	public void makeChildren()
	{
		for(KeyHolder key : kh.makeNextSetOfKeys())
			children.add(new CypherNode(this.getDepth()+1, key));
	}
	
	public ArrayList<AStarNode> getChildren() {
		if(children.size()==0)
		{
			this.makeChildren();
		}
		return children;
	}
	
	
	
	public double gethCost() {
		
		return hCost -this.getDepth();
	}
	
}
