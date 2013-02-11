package cypherSolver;

import java.util.ArrayList;

import aStarSearch.AStarNode;
import aStarSearch.ExploredNodeHolder;

public class ExploredCypherNodeHolder extends ExploredNodeHolder{
	public TextGrader grader;
	private CypherNode root;
	public double bestScore = 0;
	public ArrayList<String> usedStrings;
	public ExploredCypherNodeHolder(String txt)
	{
		grader = new TextGrader(txt);
		grader.loadWordList();
		root = new CypherNode(0, grader.makeOptimalKeyHolderFromSigleLetterFrequency());
		seenNodes.add(root);
		bestScore = 0;
		usedStrings = new ArrayList<String>();
		
	}
	
	public ExploredCypherNodeHolder(String txt, String wordList)
	{
		grader = new TextGrader(txt,wordList);
		grader.loadWordList();
		root = new CypherNode(0, grader.makeOptimalKeyHolderFromSigleLetterFrequency());
		seenNodes.add(root);
		bestScore = 0;
		usedStrings = new ArrayList<String>();
	}
	public CypherNode visitBestNode()
	{
		if(seenNodes.size()==0)
		{
			System.out.println("No more nodes");
			return null;
		}
		CypherNode bestNode  =  (CypherNode) seenNodes.get(0);
		String newKey = bestNode.getKeyHolder().getkey();
		usedStrings.add(newKey);
		seenNodes.remove(0);
		ArrayList<AStarNode> children =  bestNode.getChildren();
		ArrayList<KeyHolder> newKeys = bestNode.getKeyHoldersFromKids();
		ArrayList<Integer> reusedPositions = new ArrayList<Integer>();
		for(int ii =0; ii<newKeys.size();ii++)
		{
			if(usedStrings.contains(newKeys.get(ii).getkey()))
				reusedPositions.add(ii);
		}
		for(int ii =reusedPositions.size() -1; ii>=0;ii--)
		{
			bestNode.children.remove(reusedPositions.get(ii));
			newKeys.remove(reusedPositions.get(ii));
		}
		ArrayList<Double> scores = new ArrayList<Double>();
		for(int ii =0; ii<bestNode.children.size();ii++)
		{
			scores.add(this.grader.grade(newKeys.get(ii)));
		}
		bestNode.setChildrenCost(scores);
		for(AStarNode child : bestNode.children)
		{
			this.addNewNode(child);
		}
		return bestNode;
	}
	
}
