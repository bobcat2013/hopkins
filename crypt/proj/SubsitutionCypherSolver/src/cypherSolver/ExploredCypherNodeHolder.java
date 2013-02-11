package cypherSolver;

import java.util.ArrayList;

import aStarSearch.AStarNode;
import aStarSearch.ExploredNodeHolder;

public class ExploredCypherNodeHolder extends ExploredNodeHolder{
	public TextGrader grader;
	private CypherNode root;
	public double bestScore = 0;
	public ExploredCypherNodeHolder(String txt)
	{
		grader = new TextGrader(txt);
		grader.loadWordList();
		root = new CypherNode(0, grader.makeOptimalKeyHolderFromSigleLetterFrequency());
		seenNodes.add(root);
		bestScore = 0;
	}
	
	
	public CypherNode visitBestNode()
	{
		CypherNode bestNode  =  (CypherNode) seenNodes.get(0);
		seenNodes.remove(0);
		ArrayList<AStarNode> children =  bestNode.getChildren();
		ArrayList<KeyHolder> newKeys = bestNode.getKeyHoldersFromKids();
		ArrayList<Double> scores = new ArrayList<Double>();
		for(KeyHolder key : newKeys)
		{
			scores.add(this.grader.grade(key));
		}
		bestNode.setChildrenCost(scores);
		for(AStarNode child :children)
		{
			this.addNewNode(child);
		}
		return bestNode;
	}
	
}
