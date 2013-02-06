package cypherSolver;

import java.util.ArrayList;

import aStarSearch.AStarNode;
import aStarSearch.ExploredNodeHolder;

public class ExploredCypherNodeHolder extends ExploredNodeHolder{
	private TextGrader grader;
	public ExploredCypherNodeHolder(String txt)
	{
		grader = new TextGrader(txt);
		grader.loadWordList();
		CypherNode root = new CypherNode(0);
		root.
		grader.makeOptimalKeyHolderFromSigleLetterFrequency();
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
