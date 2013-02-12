package cypherSolver;

import aStarSearch.AStarNode;
import aStarSearch.AStarSearcher;
import aStarSearch.ExploredNodeHolder;
import aStarSearch.VistedNodeHolder;

public class CypherSearcher {

	public ExploredCypherNodeHolder ecnh;
	public VistedNodeHolder vnh; 
	public CypherSearcher(String inTxt)
	{
		ecnh = new ExploredCypherNodeHolder(inTxt);
		vnh = new VistedNodeHolder();
	}
	public CypherSearcher(String inTxt, String wl)
	{
		ecnh = new ExploredCypherNodeHolder(inTxt, wl);
		vnh = new VistedNodeHolder();
	}
	public CypherNode step()
	{
		CypherNode best = this.ecnh.visitBestNode();
		this.vnh.addNewNode(best);
		return best;
	}
	

	
}
