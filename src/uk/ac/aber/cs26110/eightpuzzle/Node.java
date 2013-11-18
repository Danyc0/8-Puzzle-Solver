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
	private int[][] startState;
	private Point start0Point;

	public Node(Direction direction, Node parent, int[][] startState, Point start0Point) {
		this.direction = direction;
		this.parent = parent;
		if(parent == null){
			this.depth = 0;
		}
		else{
			this.depth = parent.getDepth() + 1;
			this.position = findPosition();
		}
		this.startState = startState;
		this.start0Point = start0Point;
	}

	public Node(Point position, Direction direction, Node parent, int[][] startState, Point start0Point) {
		this(direction, parent, startState, start0Point);
		this.position = position;
	}

	public Node(Direction direction, Node parent, int[][] startState, Point start0Point, int[][] goalState, int whichHeuristic){
		this(direction, parent, startState, start0Point);
		this.whichHeuristic = whichHeuristic;
		this.goalState = goalState;
	}

	public Node(Direction direction, Node parent, Point position, int[][] startState, Point start0Point, int[][] goalState, int whichHeuristic) {
		this(direction, parent, startState, start0Point, goalState, whichHeuristic);
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
		/*if(this.direction == otherNode.direction && 
				this.position.getX() == otherNode.position.getX() && 
				this.position.getY() == otherNode.position.getY()){
			return true;
		}
		return false;*/
		int[][] thisSolution = this.findSolution(false);
		int[][] otherSolution = otherNode.findSolution(false);
		return compare(thisSolution, otherSolution);
	}

	private boolean compare(int[][] currentState, int[][] goalState) {
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(currentState[i][j] != goalState[i][j]){
					return false;
				}
			}
		}
		return true;
	}
	
	protected int[][] findSolution(boolean dontOutput) {
		int nodesInSolution;
		////Populate instruction stack
		Stack<Node> stack = new Stack<Node>();
		Node currentNode = this;
		stack.push(currentNode);
		while(currentNode.getParent() != null){
			currentNode = currentNode.getParent();
			stack.push(currentNode);
		}
		////Apply instructions
		int[][] currentState = deepCopy(startState);
		int currentX = (int) position.getX(); //WRONG
		int currentY = (int) position.getY(); //WRONG
		if(!dontOutput) System.out.println("New Stack of Size: " + stack.size());
		nodesInSolution = stack.size();
		this.setPathCost(nodesInSolution);
		//if(!dontOutput){
			outputStack(stack);
		//}
		while(!stack.isEmpty()){
			Node tempNode = stack.pop();
			Direction direction = tempNode.getDirection();
			if(!dontOutput) System.out.println("Moving Blank " + direction);
			switch(direction){
			case UP : {
				currentState = moveBlank(currentX, currentY, -1, 0, currentState);
				currentX--;
			}
			break;
			case DOWN : {
				currentState = moveBlank(currentX, currentY, +1, 0, currentState);
				currentX++;
			}
			break;
			case LEFT : {
				outputState(currentState);
				System.out.println("MOVING");
				currentState = moveBlank(currentX, currentY, 0, -1, currentState);
				System.out.println("MOVED");
				outputState(currentState);
				currentY--;
			}
			break;
			case RIGHT : {
				currentState = moveBlank(currentX, currentY, 0, +1, currentState);
				currentY++;
			}
			break;
			default: if(!dontOutput) System.out.println("Root");
			break;
			}
			System.out.println("TEST1");
		}
		System.out.println("TEST2");
		if(!dontOutput) outputState(currentState);
		return currentState;
	}
	
	public int[][] moveBlank(int currentX, int currentY, int xCalc, int yCalc, int[][] currentState){
		int temp = currentState[currentX][currentY];
		currentState[currentX]
				[currentY] = currentState
				[currentX + xCalc]
						[currentY + yCalc];
		currentState[currentX + xCalc][currentY + yCalc] = temp;
		return currentState;
	}
	
	protected void outputState(int[][] startState2){
		for(int[] nodes : startState2){
			for(int node : nodes){
				System.out.print(node);
			}
			System.out.println();
		}
	}

	private void outputStack(Stack<Node> stack) {
		Stack<Node> newStack = (Stack<Node>) stack.clone();
		while(!newStack.isEmpty()){
			System.out.println(newStack.pop().getDirection());
		}
	}

	private int[][] deepCopy(int[][] toBeCopied) {
		int[][] copy = new
				int[toBeCopied.length]
				[toBeCopied[0].length];
		for(int i = 0; i < copy.length; i++){
			for(int j = 0; j < copy[i].length; j++){
				copy[i][j] = toBeCopied[i][j];
			}
		}
		return copy;
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
