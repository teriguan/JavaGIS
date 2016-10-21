/**
 * Project: A00656333Gis
 * File: GameReport.java
 * Date: Jun 20, 2016
 * Time: 1:33:55 AM
 */

package a00656333.data.report.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.Game;
import a00656333.data.report.ReportData;

/**
 * Formats and outputs a game report and a total report.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class GameReport {

	private static final Logger LOG = LogManager.getLogger(Gis.class);
	// Formats the border
	private static String BORDER = String.format("%52s", " ").replaceAll(".", "-");

	/**
	 * Private constructor to prevent instantiation.
	 */
	private GameReport() {
	}

	/**
	 * Displays game report on console.
	 * 
	 * @param lines
	 *            the report data
	 */
	public static void displayGameReport(List<ReportData> lines) {
		System.out.format("%s %-18s %-15s %-8s%n", "Win:Loss", "Game Name", "Gamertag", "Platform");
		System.out.println(BORDER);

		// Formats the alignment of player info in the report
		for (ReportData line : lines) {
			System.out.format("%3d:%-4d %-18s %-15s %-8s%n", line.getWin(), line.getLoss(), line.getGameName(),
					line.getGamerTag(), line.getPlatform());
		}
		System.out.println(BORDER);

		LOG.debug("Created game report on console");
	}

	/**
	 * Displays total plays per game report on console.
	 * 
	 * @param games
	 *            the game
	 */
	public static void displayTotalReport(List<Game> games) {
		for (Game game : games) {
			System.out.format("%-18s %d%n", game.getName(), game.getPlays());
		}

		System.out.println(BORDER);

		LOG.debug("Created total report on console");
	}

	/**
	 * Outputs a game report file.
	 * 
	 * @param lines
	 *            the report data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void printGameReport(List<ReportData> lines) throws FileNotFoundException, IOException {

		PrintWriter output = new PrintWriter(new FileOutputStream("game_report.txt"));

		output.format("%s %-18s %-15s %-8s%n", "Win:Loss", "Game Name", "Gamertag", "Platform");
		output.println(BORDER);

		// Formats the alignment of player info in the report
		for (ReportData line : lines) {
			output.format("%3d:%-4d %-18s %-15s %-8s%n", line.getWin(), line.getLoss(), line.getGameName(),
					line.getGamerTag(), line.getPlatform());
		}

		output.println(BORDER);

		output.close();

		LOG.debug("Created game report file");
	}

	/**
	 * Outputs a total plays per game report file.
	 * 
	 * @param games
	 *            the game
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void printTotalReport(List<Game> games) throws FileNotFoundException, IOException {

		PrintWriter output = new PrintWriter(new FileOutputStream("game_report.txt", true));

		for (Game game : games) {
			output.format("%-18s %d%n", game.getName(), game.getPlays());
		}

		output.println(BORDER);

		output.close();

		LOG.debug("Appended total report to game report file.");
	}
}
