/**
 * Project: A00656333Gis
 * File: PlayerReader.java
 * Date: Jun 20, 2016
 * Time: 2:01:53 AM
 */

package a00656333.data.player.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.ApplicationException;
import a00656333.data.Persona;
import a00656333.data.Score;
import a00656333.data.player.Player;
import a00656333.data.util.Validator;

/**
 * @author Teresa Guan, A00656333
 *
 */

public class PlayerReader {
	private static final Logger LOG = LogManager.getLogger(Gis.class);
	private static final String ATTRIBUTE_DELIMITER = "\\|";

	/**
	 * private constructor to prevent instantiation
	 */
	private PlayerReader() {
	}

	/**
	 * Read the player data from a file.
	 * 
	 * @param playerData
	 *            The data from file
	 * @return A List of Players
	 * @throws ApplicationException
	 */
	public static List<Player> read(File file) throws ApplicationException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}

		List<Player> players = new ArrayList<Player>();
		try {
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String[] elements = scanner.nextLine().split(ATTRIBUTE_DELIMITER);
				if (elements.length == Player.FILE_ATTRIBUTE_COUNT) {
					Player player = new Player();
					int index = 0;
					player.setId(Integer.parseInt(elements[index++]));
					player.setFirstName(elements[index++]);
					player.setLastName(elements[index++]);
					String email = elements[index++];
					if (!Validator.validateEmail(email)) {
						throw new ApplicationException(String.format("\"%s\" is an invalid email.", email));
					}
					player.setEmailAddress(email);
					String yyyymmdd = elements[index];
					try {
						player.setBirthDate(Integer.parseInt(yyyymmdd.substring(0, 4)),
								Integer.parseInt(yyyymmdd.substring(4, 6)) - 1,
								Integer.parseInt(yyyymmdd.substring(6, 8)));
					} catch (NumberFormatException e) {
						LOG.error("Invalid date element:" + yyyymmdd);
					}
					players.add(player);
				} else
					throw new ApplicationException(String.format("Received %d out of %d expected elements.",
							elements.length, Player.FILE_ATTRIBUTE_COUNT));
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		LOG.debug("Player items: " + players.size());
		return players;
	}

	/**
	 * Adds the number of wins and numbers of plays to player.
	 * 
	 * @param players
	 *            The list of players.
	 * @param scores
	 *            The list of scores.
	 * @param personas
	 *            The list of personas.
	 */
	public static void addPlays(List<Player> players, List<Score> scores, List<Persona> personas) {

		for (Player player : players) {
			int totalGames = 0;
			int totalWin = 0;
			int id = player.getId();

			for (Persona persona : personas) {
				int playerId = persona.getPlayerId();
				int personaId = persona.getId();

				if (id == playerId) {
					for (Score score : scores) {
						int scoreId = score.getPersonaId();

						if (personaId == scoreId) {
							if (score.getWin()) {
								totalWin++;
								totalGames++;
							} else {
								totalGames++;
							}
						}
					}
				}
			}

			player.setGamesPlayed(totalGames);
			player.setWins(totalWin);

		}
		LOG.debug("Added wins and games to players");
	}

}
