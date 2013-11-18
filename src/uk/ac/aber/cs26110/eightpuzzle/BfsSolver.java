package uk.ac.aber.cs26110.eightpuzzle;

import java.util.ArrayList;

public class BfsSolver extends AbstractSolver {
	private MyLinkedBlockingQueue<Node> open;

	public BfsSolver(int[][] start, int[][] goal) {
		super(start, goal);
		open = new MyLinkedBlockingQueue<Node>();
	}
	
	public void start(){
		long startTime = System.nanoTime();
		Node currentNode = new Node(Direction.NONE, null, startState, startingPosition, this);
		boolean solved = false;
		open.push(currentNode);
		while(!open.isEmpty() && !solved){
			currentNode = open.pop();
			nodesEvaluated++;
			if(isSolution(currentNode)){
				solved = true;
			}
			closed.add(currentNode);
			addChildren(getChildren(currentNode));
		}
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println(nodesEvaluated + " nodes were evaluated");
		if(solved){
			System.out.println("There are " + currentNode.getPathCost() + " Nodes in the solution");
		}
		else{
			System.out.println("No Solution Was Found");
		}
		System.out.println("Time Taken: " + (totalTime/Math.pow(10,9)) + " Seconds");
	}
	
	protected void addChildren(ArrayList<Node> arrayList){
		for(Node currentChild : arrayList){
			open.push(currentChild);
		}
	}
	
	protected ArrayList<Node> addTo(ArrayList<Node> children, Direction[] directions, Node currentNode) {
		//System.out.print("Added");
		for(Direction currentDirection : directions){
			if(currentDirection != null){
				//System.out.print(" " + currentDirection);
				Node tempNode = new Node(currentDirection, currentNode, startState, this);
				if(!closedContains(tempNode)){ //TODO////THIS IS THE PROBLEM//////
					children.add(tempNode);
				}
			}
		}
		//System.out.println(" To The Queue");
		return children;
	}
}
