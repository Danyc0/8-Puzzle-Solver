package uk.ac.aber.cs26110.eightpuzzle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Importer {

	public int[][] importPuzzle(String filePath) {
		int[][] puzzleGrid = new int[3][3];
		Scanner file = null;
		try {
			file = new Scanner(new InputStreamReader (new FileInputStream(filePath)));
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		}
		String line;
		for(int i = 0; i < 3; i++){
			line = file.nextLine();
			for(int j = 1; j <= 3; j++){
				puzzleGrid[i][(j - 1)] = Character.getNumericValue(line.charAt((j * 2) - 2));
			}
		}
		file.close();
		return puzzleGrid;
	}

}
