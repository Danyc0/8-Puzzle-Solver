package uk.ac.aber.cs26110.eightpuzzle;

import java.awt.Point;

public class Node{
	private Direction direction;
	private int pathCost;
	private Node parent;
	private int heuristicValue;
	private Point position;
	private AbstractSolver solver;

	public Node(Direction direction, Node parent, int[][] startState, AbstractSolver solver) {
		this.direction = direction;
		this.parent = parent;
		this.position = findPosition();
		this.solver = solver;
	}

	public Node(Direction direction, Node parent, int[][] startState, Point position, AbstractSolver solver) {
		this(direction, parent, startState, solver);
		this.position = position;
	}

	public Node(Direction direction, Node parent, int[][] startState, int[][] goalState, AbstractSolver solver){
		this(direction, parent, startState, solver);
	}

	public Node(Direction direction, Node parent, Point position, int[][] startState, int[][] goalState, AbstractSolver solver) {
		this(direction, parent, startState, goalState, solver);
		this.position = position;
	}

	int calculateFirstHeuristic(int[][] currentState) {
		//Number of tiles out of place
		int[][] goalState = solver.getGoalState();
		int tilesOutOfPlace = 0;
		for(int i = 0; i < goalState.length; i++){
			for(int j = 0; j < goalState[0].length; j++){
				if(currentState[i][j] != goalState[i][j]){
					tilesOutOfPlace++;
				}
			}
		}
		return tilesOutOfPlace;
	}

	int calculateSecondHeuristic(int[][] currentState) {
		//Manhattan Distance
		int manhattanDistance = 0;
		for(int i = 0; i < currentState.length; i++){
			for(int j = 0; j < currentState[0].length; j++){
				Point goalLocation = find(currentState[i][j]);
				manhattanDistance =  manhattanDistance + Math.abs((i + j) - ((int)(goalLocation.getX() + goalLocation.getY())));
			}
		}
		return manhattanDistance;
	}

	private Point find(int value) {
		int [][] goalState = solver.getGoalState();
		for(int i = 0; i < goalState.length; i++){
			for(int j = 0; j < goalState[0].length; j++){
				if(goalState[i][j] == value){
					return new Point(i, j);
				}
			}
		}
		return null;
	}

	private Point findPosition() {
		Point newPosition;
		switch(direction){
		case UP : newPosition = new Point((int)(parent.getPosition().getX() - 1), (int)parent.getPosition().getY());
		break;
		case DOWN : newPosition = new Point((int)(parent.getPosition().getX() + 1), (int)parent.getPosition().getY());
		break;
		case LEFT : newPosition = new Point((int)parent.getPosition().getX(), (int)(parent.getPosition().getY() - 1));
		break;
		case RIGHT : newPosition = new Point((int)parent.getPosition().getX(), (int)(parent.getPosition().getY() + 1));
		break;
		default : {
			newPosition = null;
			System.out.println("error in Node.findPosition");
		}
		break;
		}
		return newPosition;
	}

	public Point getPosition(){
		return this.position;
	}

	public Node getParent(){
		return this.parent;
	}

	public Direction getDirection(){
		return direction;
	}

	public int getFunctionCost() {
		return (heuristicValue + pathCost);
	}

	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int newPathCost) {
		this.pathCost = newPathCost;
	}

	public void updateHeuristicValue(int[][] currentState, int whichHeuristic) {
		if(whichHeuristic == 1){
			heuristicValue = calculateFirstHeuristic(currentState);
		}
		else if(whichHeuristic == 2){
			heuristicValue = calculateSecondHeuristic(currentState);
		}
	}
}
