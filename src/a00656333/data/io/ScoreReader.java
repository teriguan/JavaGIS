/**
 * Project: A00656333Gis
 * File: ScoreReader.java
 * Date: Jun 20, 2016
 * Time: 2:03:01 AM
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
import a00656333.data.Score;

/**
 * @author Teresa Guan, A00656333
 *
 */

public class ScoreReader {

	private static final Logger LOG = LogManager.getLogger(Gis.class);
	private static final String ATTRIBUTE_DELIMITER = "\\|";
	private static final String WIN = "win";

	/**
	 * Read the score data from a file.
	 * 
	 * @param scoreData
	 *            The score data from file
	 * @return A List of Scores
	 * @throws ApplicationException
	 */
	public static List<Score> read(File file) throws ApplicationException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}

		List<Score> scores = new ArrayList<Score>();

		try {
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String[] elements = scanner.nextLine().split(ATTRIBUTE_DELIMITER);
				if (elements.length == Score.FILE_ATTRIBUTE_COUNT) {
					Score score = new Score();
					int index = 0;
					score.setPersonaId(Integer.parseInt(elements[index++]));
					score.setGameId(elements[index++]);
					score.setWin(WIN.equalsIgnoreCase(elements[index++]));
					scores.add(score);
				} else
					throw new ApplicationException(String.format("Received %d out of %d expected elements.",
							elements.length, Score.FILE_ATTRIBUTE_COUNT));
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		LOG.debug("Scores items: " + scores.size());
		return scores;
	}

}
