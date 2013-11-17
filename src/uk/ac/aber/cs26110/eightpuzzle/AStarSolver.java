package uk.ac.aber.cs26110.eightpuzzle;

import java.util.ArrayList;

public class AStarSolver extends AbstractSolver {
	private MyPriorityQueue<Node> open;
	private int whichHeuristic;

	public AStarSolver(int[][] start, int[][] goal, int whichHeuristic) {
		super(start, goal);
		NodeCostComparer comparer = new NodeCostComparer();
		open = new MyPriorityQueue<Node>(10, comparer);
		if(open.comparator() == null){
			System.out.println("BOO");
		}
		this.whichHeuristic = whichHeuristic;
	}
	
	public void start(){
		long startTime = System.nanoTime();
		Node currentNode = new Node(Direction.NONE, null, startingPosition, startState, goalState, whichHeuristic);
		boolean solved = false;
		currentNode.updateHeuristicValue(findSolution(currentNode, true));
		open.push(currentNode);
		while(!open.isEmpty() && !solved){
			currentNode = open.pop();
			nodesEvaluated++;
			System.out.println("Node Output:");
			outputState(findSolution(currentNode, true));
			//TODO OUTPUT findSolution(currentNode, true)
			System.out.println("The Costs Of This Node Are: H-" + currentNode.getHeuristicValue() + " P-" + currentNode.getPathCost() + " F-" + currentNode.getFunctionCost());
			if(isSolution(currentNode)){
				solved = true;
			}
			closed.add(currentNode);
			addChildren(getChildren(currentNode));
		}
		long endTime = System.nanoTime();
		int totalTime = (int)(endTime - startTime);
		System.out.println(nodesEvaluated + " nodes were evaluated");
		if(solved)System.out.println("There are " + currentNode.getPathCost() + " Nodes in the solution");
		else{System.out.println("No Solution Was Found");}
		System.out.println("Time Taken: " + (totalTime/Math.pow(10,9)) + "Seconds");
	}
	
	protected void addChildren(ArrayList<Node> arrayList){
		for(Node currentChild : arrayList){
			currentChild.updateHeuristicValue(findSolution(currentChild, true));
			open.push(currentChild);
		}
	}
	
	protected ArrayList<Node> addTo(ArrayList<Node> children, Direction[] directions, Node currentNode) {
		System.out.print("Added");
		for(Direction currentDirection : directions){
			if(currentDirection != null){
				System.out.print(" " + currentDirection);
				//TODO OUTPUT findSolution(currentNode, true)
				Node tempNode = new Node(currentDirection, currentNode, findSolution(currentNode, true), goalState, whichHeuristic);
				children.add(tempNode);
			}
		}
		System.out.println(" To The Priority Queue");
		return children;
	}
}
