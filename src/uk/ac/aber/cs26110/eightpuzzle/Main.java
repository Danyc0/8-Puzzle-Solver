package uk.ac.aber.cs26110.eightpuzzle;

public class Main {

	public static void main(String[] args) {
		String startPath = args[0];
		String goalPath = args[1];
		String mode = args[2];
		Importer importer = new Importer();
		int[][] start = importer.importPuzzle(startPath);
		int[][] goal = importer.importPuzzle(goalPath);
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
