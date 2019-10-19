package by.redsky.earthshaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import by.redsky.framework.interfaces.FileIO;
import android.util.Log;

public class Settings {

	public static boolean soundEnable = true;
	public static int[] highscores = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public static int scoreTotal;

	public static void load(FileIO file) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					file.readFile("./earthshaker.jso")));
			soundEnable = Boolean.parseBoolean(in.readLine());
			for (int i = 0; i < 10; i++) {
				highscores[i] = Integer.parseInt(in.readLine());
			}
		} catch (IOException e) {
			Log.d("lol", "Read Error");
		} catch (NumberFormatException e) {
			Log.d("lol", "Parser Error");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e2) {
			}
		}
	}

	public static void save(FileIO file) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					file.writeFile("./earthshaker.jso")));
			out.write(Boolean.toString(soundEnable) + "\n");
			for (int i = 0; i < 10; i++) {
				out.write(Integer.toString(highscores[i]) + "\n");
			}
		} catch (IOException e) {
			Log.d("lol", "Write Error");
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void addScore(int score) {
		for (int i = 0; i < 10; i++) {
			if (highscores[i] < score) {
				for (int j = 9; j > i; j--) {
					highscores[j] = highscores[j - 1];
				}
				highscores[i] = score;
				break;
			}
		}
	}

}
