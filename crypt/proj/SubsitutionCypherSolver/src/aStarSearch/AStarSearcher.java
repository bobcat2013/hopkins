package aStarSearch;

public class AStarSearcher {

	protected ExploredNodeHolder exploredNodes;
	private VistedNodeHolder vistNodes;
	public AStarSearcher(ExploredNodeHolder enh)
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
