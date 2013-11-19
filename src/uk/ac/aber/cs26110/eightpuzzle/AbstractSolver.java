package uk.ac.aber.cs26110.eightpuzzle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

public abstract class AbstractSolver {
	
	int[][] startState;
	Point startingPosition;
	int[][] goalState;
	ArrayList<int[][]> closed;
	int nodesEvaluated = 0;
	int nodesInSolution = 0;
	
	public AbstractSolver(int[][] start, int[][] goal) {
		this.startState = start;
		startingPosition = getStartingPosition();
		this.goalState = goal;
		closed = new ArrayList<int[][]>();
		outputState(startState);
		outputState(goalState);
	}
	
	private Point getStartingPosition() {
		for(int i = 0; i < startState.length; i++){
			for(int j = 0; j < startState[0].length; j++){
				if(startState[i][j] == 0){
					return new Point(i, j);
				}
			}
		}
		return null;
	}

	abstract void start();

	abstract void addChildren(ArrayList<Node> arrayList);

	protected ArrayList<Node> getChildren(Node currentNode) {
		Point currentPosition = currentNode.getPosition();
		ArrayList<Node> children = null;
		Point[] points = { //Make a constant?
				new Point(0, 0),
				new Point(0, 1),
				new Point(0, 2),
				new Point(1, 0),
				new Point(1, 1),
				new Point(1, 2),
				new Point(2, 0),
				new Point(2, 1),
				new Point(2, 2)
			};
		children = new ArrayList<Node>();
		Direction[] directions = new Direction[4];
		if(comparePositions(currentPosition, points[0])){
			directions[0] = Direction.RIGHT;
			directions[1] = Direction.DOWN;
		}
		else if(comparePositions(currentPosition, points[1])){
			directions[0] = Direction.RIGHT;
			directions[1] = Direction.LEFT;
			directions[2] = Direction.DOWN;
		}
		else if(comparePositions(currentPosition, points[2])){
			directions[0] = Direction.LEFT;
			directions[1] = Direction.DOWN;
		}
		else if(comparePositions(currentPosition, points[3])){
			directions[0] = Direction.RIGHT;
			directions[1] = Direction.UP;
			directions[2] = Direction.DOWN;
		}
		else if(comparePositions(currentPosition, points[4])){
			directions[0] = Direction.RIGHT;
			directions[1] = Direction.LEFT;
			directions[2] = Direction.UP;
			directions[3] = Direction.DOWN;
		}
		else if(comparePositions(currentPosition, points[5])){
			directions[0] = Direction.LEFT;
			directions[1] = Direction.UP;
			directions[2] = Direction.DOWN;
		}
		else if(comparePositions(currentPosition, points[6])){
			directions[0] = Direction.RIGHT;
			directions[1] = Direction.UP;
		}
		else if(comparePositions(currentPosition, points[7])){
			directions[0] = Direction.RIGHT;
			directions[1] = Direction.LEFT;
			directions[2] = Direction.UP;
		}
		else if(comparePositions(currentPosition, points[8])){
			directions[0] = Direction.LEFT;
			directions[1] = Direction.UP;
		}
		children = addTo(children, directions, currentNode);
		return children;
	}

	abstract ArrayList<Node> addTo(ArrayList<Node> children, Direction[] directions, Node currentNode);

	protected boolean closedContains(Node tempNode) {
		for(int[][] currentState : closed){
			if(compareStateWithNode(currentState, tempNode)){
				return true;
			}
		}
		return false;
	}
	
	private boolean compareStateWithNode(int[][] currentState, Node tempNode) {
		int[][] tempState = findSolution(tempNode, true);
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(currentState[i][j] != tempState[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	private boolean comparePositions(Point point1, Point point2) {
		return ((point1.getX() == point2.getX()) && (point1.getY() == point2.getY()));
	}

	public boolean isSolution(Node currentNode){
		int[][] currentState = findSolution(currentNode, true);
		//Check if it has reached goal state
		return compare(currentState, goalState, true);
	}

	protected int[][] findSolution(Node startNode, boolean dontOutput) {
		int nodesInSolution;
		////Populate instruction stack
		Stack<Node> stack = new Stack<Node>();
		Node currentNode = startNode;
		stack.push(currentNode);
		while(currentNode.getParent() != null){
			currentNode = currentNode.getParent();
			stack.push(currentNode);
		}
		////Apply instructions
		int[][] currentState = deepCopy(startState);
		int currentX = (int) startingPosition.getX();
		int currentY = (int) startingPosition.getY();
		if(!dontOutput){
			System.out.println("New Stack of Size: " + stack.size());
		}
		nodesInSolution = stack.size();
		startNode.setPathCost(nodesInSolution);
		if(!dontOutput){
			outputStack(stack);
		}
		while(!stack.isEmpty()){
			Node tempNode = stack.pop();
			Direction direction = tempNode.getDirection();
			if(!dontOutput){
				System.out.println("Moving Blank " + direction);
			}
			switch(direction){
			case UP : {
				moveBlank(currentX, currentY, -1, 0, currentState);
				currentX--;
			}
			break;
			case DOWN : {
				moveBlank(currentX, currentY, +1, 0, currentState);
				currentX++;
			}
			break;
			case LEFT : {
				moveBlank(currentX, currentY, 0, -1, currentState);
				currentY--;
			}
			break;
			case RIGHT : {
				moveBlank(currentX, currentY, 0, +1, currentState);
				currentY++;
			}
			break;
			default: if(!dontOutput){
				System.out.println("Root");
			}
			break;
			}
		}
		if(!dontOutput){
			outputState(currentState);
		}
		return currentState;
	}
	
	public void moveBlank(int currentX, int currentY, int xCalc, int yCalc, int[][] currentState){
		int temp = currentState[currentX][currentY];
		currentState[currentX][currentY] = currentState[currentX + xCalc][currentY + yCalc];
		currentState[currentX + xCalc][currentY + yCalc] = temp;
	}

	private void outputStack(Stack<Node> stack) {
		Stack<Node> newStack = (Stack<Node>) stack.clone();
		while(!newStack.isEmpty()){
			System.out.println(newStack.pop().getDirection());
		}
	}

	private int[][] deepCopy(int[][] toBeCopied) {
		int[][] copy = new int[toBeCopied.length][toBeCopied[0].length];
		for(int i = 0; i < copy.length; i++){
			for(int j = 0; j < copy[i].length; j++){
				copy[i][j] = toBeCopied[i][j];
			}
		}
		return copy;
	}
	
	protected void outputState(int[][] startState2){
		for(int[] nodes : startState2){
			for(int node : nodes){
				System.out.print(node);
			}
			System.out.println();
		}
	}

	private int fails = 1;
	private boolean compare(int[][] currentState, int[][] goalState, boolean dontOutput) {
		if(!dontOutput){
			outputState(currentState);
		}
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(currentState[i][j] != goalState[i][j]){
					System.out.println("Not A Solution. " + fails + " fails");
					fails++;
					return false;
				}
			}
		}
		System.out.println("Solution Found");
		outputState(currentState);
		return true;
	}
	
	public int[][] getGoalState(){
		return goalState;
	}
	
	protected void review(long startTime, long endTime, boolean solved, Node solutionNode){
		Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
		long totalTime = endTime - startTime;
		System.out.println(nodesEvaluated + " nodes were evaluated");
		if(solved){
			System.out.println("There are " + solutionNode.getPathCost() + " Nodes in the solution");
		}
		else{
			System.out.println("No Solution Was Found");
		}
		System.out.println("Time Taken: " + (totalTime/Math.pow(10,9)) + " Seconds");
		System.out.println("Memory used: " + (memory/Math.pow(10,6)) + " MegaBytes");
	}
}
