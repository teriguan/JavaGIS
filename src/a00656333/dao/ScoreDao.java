/**
 * Project: A00656333Gis
 * File: ScoreDao.java
 * Date: Jul 14, 2016
 * Time: 10:07:26 PM
 */

package a00656333.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.ApplicationException;
import a00656333.data.Database;
import a00656333.data.Score;
import a00656333.data.io.ScoreReader;

/**
 * Creates Score table and populates with score attributes.
 * 
 * @author Teresa Guan, A00656333
 * @author scirka
 *
 */

public class ScoreDao extends Dao {

	private static final String SCORES_DATA_FILENAME = "scores.dat";

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	public static final String TABLE_NAME = "Score";

	public ScoreDao() {
		super(Database.getInstance(), TABLE_NAME);
	}

	@Override
	public void create() throws SQLException {
		String createStatement = String.format(
				"create table %s(%s INT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1,INCREMENT BY 1) CONSTRAINT PRIMARY_KEY primary key, %s VARCHAR(5), %s VARCHAR(4), %s VARCHAR(4) )",
				tableName, //
				Fields.SCORE_ID, Fields.PERSONA_ID, Fields.GAME_ID, Fields.WIN);
		super.create(createStatement);
		LOG.debug("Created table");
	}

	public void add(Score score) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s(%s, %s, %s) values('%s', '%s', '%s')", tableName, //
					Fields.PERSONA_ID, //
					Fields.GAME_ID, //
					Fields.WIN, //
					"" + score.getPersonaId(), //
					score.getGameId(), //
					score.getWinString());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	public List<Integer> getScoreIDs() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<Integer> scoreIds = new ArrayList<Integer>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// Get the score ids to a list
			while (resultSet.next()) {
				int scoreId = resultSet.getInt(Fields.SCORE_ID.name());

				scoreIds.add(scoreId);
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s", tableName));
		LOG.debug(String.format("Loaded %d score ids from database", scoreIds.size()));
		return scoreIds;
	}

	public Score getScore(int scoreId) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Score score = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement(); // Execute a statement
			StringBuilder str = new StringBuilder(String.format("SELECT * FROM %s", tableName));
			str.append(String.format("WHERE %s = %s", Fields.SCORE_ID, scoreId));
			// str.append();
			String sqlString = str.toString();
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Score
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				score = new Score();
				score.setPersonaId(resultSet.getInt(Fields.PERSONA_ID.name()));
				score.setGameId(resultSet.getString(Fields.GAME_ID.name()));
				score.setWin(resultSet.getString(Fields.WIN.name()));
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.SCORE_ID, scoreId));
		LOG.debug(Instant.now());
		return score;
	}

	/**
	 * The String of game plays of each game for total plays report.
	 * 
	 * @return String of game plays
	 * @throws SQLException
	 */
	public String getGamePlays() throws SQLException {
		Connection connection;
		Statement statement = null;
		String totalPlays = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			// String sqlString = "SELECT Count(SCORE_ID) AS total, NAME FROM
			// Score INNER JOIN Game on Score.GAME_ID = ID GROUP BY GAME_ID,
			// NAME";

			StringBuilder str = new StringBuilder(String.format("SELECT %s, COUNT(%s) AS total FROM %s",
					GameDao.Fields.NAME, Fields.SCORE_ID, tableName));
			str.append(String.format(" INNER JOIN %s ON %s.%s = %s", GameDao.TABLE_NAME, tableName, Fields.GAME_ID,
					GameDao.Fields.ID));
			str.append(String.format(" GROUP BY %s, %s", Fields.GAME_ID, GameDao.Fields.NAME));

			String sqlString = str.toString();

			ResultSet resultSet = statement.executeQuery(sqlString);
			StringBuilder tempStr = new StringBuilder();
			// Get the score ids to a list
			while (resultSet.next()) {
				String str1 = String.format("%s %d%n", resultSet.getString(GameDao.Fields.NAME.name()),
						resultSet.getInt("total"));

				tempStr.append(str1);

			}

			totalPlays = tempStr.toString();
		} finally {
			close(statement);
		}
		LOG.debug("Loaded total game plays from database");
		return totalPlays;
	}

	/**
	 * List of score objects for the score report.
	 * 
	 * @return List of score objects
	 * @throws SQLException
	 * @throws ApplicationException
	 */
	public List<Score> getScoreData() throws SQLException, ApplicationException {
		Connection connection;
		Statement statement = null;
		List<Score> scoreData = new ArrayList<Score>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			// String sqlString = "SELECT Count(SCORE_ID) AS total, NAME FROM
			// Score INNER JOIN Game on Score.GAME_ID = ID GROUP BY GAME_ID,
			// NAME";

			StringBuilder str = new StringBuilder(String.format("SELECT * FROM %s", tableName));
			str.append(String.format(" INNER JOIN %s ON %s.%s = %s.%s", PersonaDao.TABLE_NAME, tableName,
					Fields.PERSONA_ID, PersonaDao.TABLE_NAME, PersonaDao.Fields.ID));
			str.append(String.format(" INNER JOIN %s ON %s.%s = %s.%s", GameDao.TABLE_NAME, tableName, Fields.GAME_ID,
					GameDao.TABLE_NAME, GameDao.Fields.ID));

			String sqlString = str.toString();

			ResultSet resultSet = statement.executeQuery(sqlString);

			// Get the score data to a list
			while (resultSet.next()) {
				Score score = new Score();
				score.setPersonaName(String.format("(%s) %s", resultSet.getInt(Fields.PERSONA_ID.name()),
						resultSet.getString(PersonaDao.Fields.GAMERTAG.name())));
				score.setGameName(resultSet.getString(GameDao.Fields.NAME.name()));
				score.setWin(resultSet.getString(Fields.WIN.name()));

				scoreData.add(score);

			}

		} finally {
			close(statement);
		}
		LOG.debug("Loaded total game plays from database");
		return scoreData;
	}

	public void update(Score score) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s' WHERE %s='%s', %s='%s', %s='%s'",
					tableName, //
					Fields.PERSONA_ID, score.getPersonaId(), //
					Fields.GAME_ID, score.getGameId(), //
					Fields.WIN, score.getWinString(), //
					Fields.PERSONA_ID, score.getPersonaId(), //
					Fields.GAME_ID, score.getGameId(), //
					Fields.WIN, score.getWinString());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void delete(Score score) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s', %s='%s', %s='%s'", tableName,
					Fields.PERSONA_ID, score.getPersonaId(), Fields.GAME_ID, score.getGameId(), Fields.WIN,
					score.getWinString());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void init() throws ApplicationException {
		try {
			if (!Database.tableExists(ScoreDao.TABLE_NAME)) {
				File file = new File(SCORES_DATA_FILENAME);
				if (!file.exists()) {
					LOG.error(String.format("Required '%s' is missing.", SCORES_DATA_FILENAME));
					System.exit(-1);
				}

				int scoreCount = 0;
				create();
				List<Score> scores = ScoreReader.read(file);
				for (Score score : scores) {
					add(score);
					scoreCount++;
				}

				LOG.debug("Inserted " + scoreCount + " scores");
			}
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public enum Fields {

		SCORE_ID(1), PERSONA_ID(2), GAME_ID(3), WIN(4);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}

}