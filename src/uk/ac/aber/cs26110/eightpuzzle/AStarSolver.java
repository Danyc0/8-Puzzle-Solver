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
		Node currentNode = new Node(Direction.NONE, null, startingPosition, startState, goalState, this);
		boolean solved = false;
		currentNode.updateHeuristicValue(findSolution(currentNode, true), whichHeuristic);
		open.push(currentNode);
		while(!open.isEmpty() && !solved){
			currentNode = open.pop();
			nodesEvaluated++;
			System.out.println("Node Output:");
			outputState(findSolution(currentNode, true));
			if(isSolution(currentNode)){
				solved = true;
			}
			addChildren(getChildren(currentNode));
		}

		long endTime = System.nanoTime();
		review(startTime, endTime, solved, currentNode);
	}
	
	protected void addChildren(ArrayList<Node> arrayList){
		for(Node currentChild : arrayList){
			currentChild.updateHeuristicValue(findSolution(currentChild, true), whichHeuristic);
			open.push(currentChild);
		}
	}
	
	protected ArrayList<Node> addTo(ArrayList<Node> children, Direction[] directions, Node currentNode) {
		for(Direction currentDirection : directions){
			if(currentDirection != null){
				Node tempNode = new Node(currentDirection, currentNode, findSolution(currentNode, true), goalState, this);
				children.add(tempNode);
			}
		}
		return children;
	}
}
