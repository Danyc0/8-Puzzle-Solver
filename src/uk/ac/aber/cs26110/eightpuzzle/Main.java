package uk.ac.aber.cs26110.eightpuzzle;

public class Main {

	public static void main(String[] args){//String startPath, String goalPath, String mode) {
		String mode = "dfs";
		Importer importer = new Importer();
		int[][] start = importer.importPuzzle("/home/daniel/Documents/CS261/datafiles/testStart3.txt");//startPath);
		int[][] goal = importer.importPuzzle("/home/daniel/Documents/CS261/datafiles/testGoal3.txt");//goalPath);
		AbstractSolver solver = null;
		switch(mode){
		case "bfs" : solver = new BfsSolver(start, goal);
		break;
		case "dfs" : solver = new DfsSolver(start, goal);
		break;
		case "astar1" : solver = new AStarSolver(start, goal, 1);
		break;
		case "astar2" : solver = new AStarSolver(start, goal, 2);
		break;
		}
		solver.start();
	}
}
