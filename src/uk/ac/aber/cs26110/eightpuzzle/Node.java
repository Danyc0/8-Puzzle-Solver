package uk.ac.aber.cs26110.eightpuzzle;

import java.awt.Point;
import java.util.Stack;

public class Node{
	private Direction direction;
	private int pathCost;
	private Node parent;
	private int heuristicValue;
	private int depth;
	private Point position;
	private int whichHeuristic;
	private int[][] goalState;

	public Node(Direction direction, Node parent) {
		this.direction = direction;
		this.parent = parent;
		if(parent == null){
			this.depth = 0;
		}
		else{
			this.depth = parent.getDepth() + 1;
			this.position = findPosition();
		}
	}
	
	public Node(Direction direction, Node parent, Point position) {
		this(direction, parent);
		this.position = position;
	}
	
	public Node(Direction direction, Node parent, int[][] currentState, int[][] goalState, int whichHeuristic){
		this(direction, parent);
		this.whichHeuristic = whichHeuristic;
		this.goalState = goalState;
	}

	public Node(Direction direction, Node parent, Point position, int[][] currentState, int[][] goalState, int whichHeuristic) {
		this(direction, parent, currentState, goalState, whichHeuristic);
		this.position = position;
	}
	
	int calculateFirstHeuristic(int[][] currentState) {
		//Number of tiles out of place
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
		// TODO Manhattan Distance
		int manhattanDistance = 0;
		for(int i = 0; i < currentState.length; i++){
			for(int j = 0; j < currentState[0].length; j++){
				Point goalLocation = find(currentState[i][j], goalState);
				manhattanDistance =  manhattanDistance + Math.abs((i + j) - ((int)(goalLocation.getX() + goalLocation.getY())));
			}
		}
		return manhattanDistance;
	}

	private Point find(int value, int[][] goalState) {
		for(int i = 0; i < goalState.length; i++){
			for(int j = 0; j < goalState[0].length; j++){
				if(goalState[i][j] == value){
					return new Point(i, j);
				}
			}
		}
		return null;
	}

	public boolean equals(Node otherNode){ 	//PUT IN ABOUT WHERE THE PARENTS ARE DOING THE SAME THING
											//MAYBE JUST EVAUATE BOTH AND CHECK IF THE RESULTS ARE THE SAME
		
		if(this.direction == otherNode.direction && this.position.getX() == otherNode.position.getX() && this.position.getY() == otherNode.position.getY()){
			return true;
		}
		return false;
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

	public int getDepth(){
		return this.depth;
	}

	public int getFunctionCost() {
		return (heuristicValue + pathCost);
	}

	public int getHeuristicValue() {
		return heuristicValue;
	}

	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int newPathCost) {
		this.pathCost = newPathCost;
	}

	public void updateHeuristicValue(int[][] currentState) {
		if(whichHeuristic == 1){
			heuristicValue = calculateFirstHeuristic(currentState);
		}
		else if(whichHeuristic == 2){
			heuristicValue = calculateSecondHeuristic(currentState);
		}
	}
}
