package aStarSearch;

public class AStarSearcher {

	private ExploredNodeHolder exploredNodes;
	private VistedNodeHolder vistNodes;
	public AStarSearcher(AStarNode inRoot, ExploredNodeHolder enh)
	{
		exploredNodes=enh;
		vistNodes = new VistedNodeHolder();
	}
	
	public AStarNode step()
	{
		AStarNode best = this.exploredNodes.visitBestNode();
		this.vistNodes.addNewNode(best);
		return best;
	}

	}
