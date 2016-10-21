/**
 * Project: A00656333Gis
 * File: PlayerReport.java
 * Date: Jun 20, 2016
 * Time: 1:31:59 AM
 */

package a00656333.data.player.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.player.Player;

/**
 * Formats and outputs players report.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class PlayerReport {

	private static final Logger LOG = LogManager.getLogger(Gis.class);
	private static final String BORDER = String.format("%95s", " ").replaceAll(".", "-");

	/**
	 * Private constructor to prevent instantiation.
	 */
	private PlayerReport() {
	}

	public static void displayPlayerReport(List<Player> players) {

		System.out.format("%10s  %-20s %-25s %-4s %-20s %10s%n", "Player ID", "Full Name", "Email", "Age",
				"Total Games Played", "Total Wins");
		System.out.println(BORDER);

		// Formats the alignment of player info in the report
		for (Player player : players) {
			System.out.format("%10d  %-20s %-25s %3d %19d %12d%n", player.getId(),
					player.getFirstName() + " " + player.getLastName(), player.getEmailAddress(),
					getAge(player.getBirthDate()), player.getGamesPlayed(), player.getWins());
		}
		System.out.println(BORDER);
		LOG.debug("Created player report on console.");
	}

	/**
	 * Outputs a player report file.
	 * 
	 * @param players
	 *            The players to file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void printReportFile(List<Player> players) throws FileNotFoundException, IOException {

		PrintWriter output = new PrintWriter(new FileOutputStream("players_report.txt"));

		output.format("%10s  %-20s %-25s %-4s %-20s %10s%n", "Player ID", "Full Name", "Email", "Age",
				"Total Games Played", "Total Wins");
		output.println(BORDER);

		// Formats the alignment of player info in the report
		for (Player player : players) {
			output.format("%10d  %-20s %-25s %3d %19d %12d%n", player.getId(),
					player.getFirstName() + " " + player.getLastName(), player.getEmailAddress(),
					getAge(player.getBirthDate()), player.getGamesPlayed(), player.getWins());
		}
		output.println(BORDER);
		output.close();
		LOG.debug("Created player report file.");
	}

	/**
	 * Calculate a player's age using the birthdate.
	 * 
	 * @param inputDate
	 *            The input birthdate.
	 * @return the age.
	 */
	private static int getAge(LocalDate inputDate) {
		LocalDate now = LocalDate.now();
		Period period = Period.between(inputDate, now);
		int age = period.getYears();

		return age;
	}
}
