package aStarSearch;

public class AStarSearcher {

	public AStarNode root;
	private ExploredNodeHolder exploredNodes;
	private VistedNodeHolder vistNodes;
	public AStarSearcher(AStarNode inRoot)
	{
		root = inRoot;
	}
	
	public AStarNode step()
	{
		AStarNode best = this.exploredNodes.visitBestNode();
		this.vistNodes.addNewNode(best);
		return best;
	}

	}
