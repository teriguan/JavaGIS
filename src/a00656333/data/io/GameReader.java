/**
 * Project: A00656333Gis
 * File: GameReader.java
 * Date: Jun 20, 2016
 * Time: 2:02:20 AM
 */

package a00656333.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.ApplicationException;
import a00656333.data.Game;
import a00656333.data.report.ReportData;

/**
 * @author Teresa Guan, A00656333
 *
 */

public class GameReader {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	private static final String ATTRIBUTE_DELIMITER = "\\|";

	/**
	 * Read the game data from a file.
	 * 
	 * @param playerData
	 *            The data from file
	 * @return A List of Players
	 * @throws ApplicationException
	 */
	public static List<Game> read(File file) throws ApplicationException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}

		List<Game> games = new ArrayList<Game>();

		try {
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String[] elements = scanner.nextLine().split(ATTRIBUTE_DELIMITER);
				if (elements.length == Game.FILE_ATTRIBUTE_COUNT) {
					Game game = new Game();
					int index = 0;
					game.setId(elements[index++]);
					game.setName(elements[index++]);
					game.setProducer(elements[index++]);

					games.add(game);
				} else {
					throw new ApplicationException(String.format("Received %d out of %d expected elements.",
							elements.length, Game.FILE_ATTRIBUTE_COUNT));
				}
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		LOG.debug("Game items: " + games.size());
		return games;
	}

	/**
	 * Add total game plays to game object.
	 * 
	 * @param games
	 *            the game object
	 * @param reports
	 *            the report data object
	 */
	public static void addPlays(List<Game> games, List<ReportData> reports) {

		for (Game game : games) {
			String name = game.getName();
			int play = 0;

			for (ReportData report : reports) {
				String gameName = report.getGameName();
				if (name.equals(gameName)) {
					play += report.getWin() + report.getLoss();
				}
			}
			game.setPlays(play);
		}

		LOG.debug("Added plays to games");
	}

}
