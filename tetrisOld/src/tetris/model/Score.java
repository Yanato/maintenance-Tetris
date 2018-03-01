package tetris.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Score {

	private int allscore[] = {1,2,3};
	private int numberline;
	
	
	
	public int getAllscore(int rank) {

		return allscore[rank];
	}


	public void testingNewScore(int newScore) {
		load();
		sortValues();
		isGreater(newScore);
		save();
		load();
	}
	
	
	private void sortValues() {
//carreful, 0 allscore[0] is the lower and 3 is the best
        Arrays.sort(allscore);

	}
	private void isGreater(int testedValue) {
		if(testedValue >= allscore[0]) {
			allscore[0] = testedValue;
		}	}

	
	
	public void load() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("score.txt"));
			String line = reader.readLine();
			numberline = 0;
			while (line != null) {
				allscore[numberline] = Integer.parseInt(line);
				//System.out.println(allscore[numberline]);
				// read next line
				line = reader.readLine();
				numberline++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void save() {
		try {
			File fout = new File("score.txt");
			fout.createNewFile(); // if the file already exists, nothing will happen
			FileOutputStream fos = new FileOutputStream(fout, false); 
			//System.out.println("Path : " + fout.getAbsolutePath()); chemin du fichier

			OutputStreamWriter osw = new OutputStreamWriter(fos);

			for (int i = 0; i<3; i++  ) {
				osw.write(String.valueOf(allscore[i]));
				osw.write('\n');

			}
			
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}