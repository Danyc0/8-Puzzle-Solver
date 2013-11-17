package uk.ac.aber.cs26110.eightpuzzle;

import java.util.Comparator;

public class NodeCostComparer implements Comparator<Node> {

	@Override
	public int compare(Node node1, Node node2){
		if(node1.getFunctionCost() < node2.getFunctionCost()){
			return -1;
		}
		else if(node1.getFunctionCost() > node2.getFunctionCost()){
			return 1;
		}
		return 0;
	}

}
