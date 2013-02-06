package cypherSolver;

import java.util.ArrayList;

import aStarSearch.AStarNode;

public class CypherNode extends AStarNode {

	private KeyHolder kh;

	
	public CypherNode(int steps, KeyHolder inKH) {
		super(steps);
		kh = inKH;
	}

	public void makeChildren()
	{
		for(KeyHolder key : kh.makeNextSetOfKeys())
			children.add(new CypherNode(this.getDepth()+1, key));
	}
	
	public ArrayList<AStarNode> getChildren() {
		if(children.size()==0)
		{
			makeChildren();
		}
		return children;
	}
	
	
	
	public double gethCost() {
		
		return hCost;
	}
	
}
