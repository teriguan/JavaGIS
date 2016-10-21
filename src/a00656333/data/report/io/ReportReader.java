/**
 * Project: A00656333Gis
 * File: GameDataReader.java
 * Date: Jun 21, 2016
 * Time: 7:21:08 PM
 */

package a00656333.data.report.io;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.ApplicationException;
import a00656333.data.Game;
import a00656333.data.Persona;
import a00656333.data.Score;
import a00656333.data.report.ReportData;

/**
 * Creates game report data objects.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class ReportReader {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	/**
	 * Private constructor to prevent instantiation.
	 */
	private ReportReader() {

	}

	/**
	 * Constructs game report data from scores, personas, and games.
	 * 
	 * @param reportData
	 * @return A List of report data
	 * @throws ApplicationException
	 */
	public static List<ReportData> read(List<Score> scores, List<Persona> personas, List<Game> games)
			throws ApplicationException {

		Set<ReportData> temp = new HashSet<ReportData>();

		for (Score score : scores) {

			int win = 0;
			int loss = 0;
			String gamerTag = null;
			String platform = null;
			String gameName = null;

			for (Persona persona : personas) {
				if (score.getPersonaId() == persona.getId()) {
					gamerTag = persona.getGamerTag();
					platform = persona.getPlatform();
				}

			}

			for (Game game : games) {
				if (score.getGameId().equals(game.getId())) {
					gameName = game.getName();
				}
			}

			ReportData line = new ReportData();

			line.setWin(win);
			line.setLoss(loss);
			line.setGameName(gameName);
			line.setGamerTag(gamerTag);
			line.setPlatform(platform);
			temp.add(line);

		}
		LOG.debug("Game data items: " + temp.size());
		List<ReportData> reportData = new ArrayList<ReportData>(temp);
		addWinLoss(reportData, scores, personas, games);
		return reportData;
	}

	/**
	 * Adds in the wins and losses to report data.
	 * 
	 * @param reportData
	 *            the report data
	 * @param scores
	 *            the scores
	 * @param personas
	 *            the personas
	 * @param games
	 *            the games
	 */
	private static void addWinLoss(List<ReportData> reportData, List<Score> scores, List<Persona> personas,
			List<Game> games) {

		for (ReportData report : reportData) {
			String gamerTag = report.getGamerTag();
			String platform = report.getPlatform();
			String gameName = report.getGameName();
			int win = 0;
			int loss = 0;

			for (Score score : scores) {
				String scoreGamerTag = null;
				String scorePlatform = null;
				String scoreGameName = null;

				for (Persona persona : personas) {
					if (score.getPersonaId() == persona.getId()) {
						scoreGamerTag = persona.getGamerTag();
						scorePlatform = persona.getPlatform();
					}
				}

				for (Game game : games) {
					if (score.getGameId().equals(game.getId())) {
						scoreGameName = game.getName();
					}
				}

				if (gamerTag.equals(scoreGamerTag) && platform.equals(scorePlatform)
						&& gameName.equals(scoreGameName)) {
					if (score.getWin()) {
						win++;
					} else {
						loss++;
					}
				}
			}
			report.setWin(win);
			report.setLoss(loss);
		}
		LOG.debug("Added wins and losses to game report data");
	}
}
