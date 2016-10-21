/**
 * Project: A00656333Gis
 * File: ReportDao.java
 * Date: Jul 19, 2016
 * Time: 11:15:00 AM
 */

package a00656333.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.ApplicationException;
import a00656333.data.Database;
import a00656333.data.Game;
import a00656333.data.Persona;
import a00656333.data.Score;
import a00656333.data.io.GameReader;
import a00656333.data.io.PersonaReader;
import a00656333.data.io.ScoreReader;
import a00656333.data.report.ReportData;
import a00656333.data.report.io.ReportReader;

/**
 * Creates Player database and populates with player attributes.
 * 
 * @author Teresa Guan, A00656333
 * @author scirka
 *
 */

public class ReportDao extends Dao {

	private static final String PERSONAS_DATA_FILENAME = "personas.dat";
	private static final String GAMES_DATA_FILENAME = "games.dat";
	private static final String SCORES_DATA_FILENAME = "scores.dat";

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	public static final String TABLE_NAME = "Report";

	public ReportDao() {
		super(Database.getInstance(), TABLE_NAME);
	}

	@Override
	public void create() throws SQLException {
		String createStatement = String.format(
				"create table %s(%s VARCHAR(20) NOT NULL, %s VARCHAR(20) NOT NULL, %s VARCHAR(3) NOT NULL, %s VARCHAR(5), %s VARCHAR(5), primary key (%s , %s, %s) )",
				tableName, //
				Fields.GAME_NAME, Fields.GAMERTAG, Fields.PLATFORM, Fields.WIN, Fields.LOSS, Fields.GAME_NAME,
				Fields.GAMERTAG, Fields.PLATFORM);
		super.create(createStatement);
	}

	public void add(ReportData reportData) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', '%s', '%s', '%s', '%s')", tableName, //
					"" + reportData.getGameName(), //
					reportData.getGamerTag(), //
					reportData.getPlatform(), //
					reportData.getWin(), //
					reportData.getLoss());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	public List<ReportData> getReport() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<ReportData> reports = new ArrayList<ReportData>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// Get the score ids to a list
			while (resultSet.next()) {
				ReportData report = new ReportData();
				report.setGameName(resultSet.getString(Fields.GAME_NAME.name()));
				report.setGamerTag(resultSet.getString(Fields.GAMERTAG.name()));
				report.setPlatform(resultSet.getString(Fields.PLATFORM.name()));
				report.setWin(resultSet.getInt(Fields.WIN.name()));
				report.setLoss(resultSet.getInt(Fields.LOSS.name()));

				reports.add(report);
			}
		} finally {
			close(statement);
		}
		return reports;
	}

	public List<ReportData> getReportByGamertag(String gamerTag) throws SQLException {
		Connection connection;
		Statement statement = null;
		List<ReportData> reports = new ArrayList<ReportData>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s='%s'", tableName, Fields.GAMERTAG, gamerTag);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// Get the score ids to a list
			while (resultSet.next()) {
				ReportData report = new ReportData();
				report.setGameName(resultSet.getString(Fields.GAME_NAME.name()));
				report.setGamerTag(resultSet.getString(Fields.GAMERTAG.name()));
				report.setPlatform(resultSet.getString(Fields.PLATFORM.name()));
				report.setWin(resultSet.getInt(Fields.WIN.name()));
				report.setLoss(resultSet.getInt(Fields.LOSS.name()));

				reports.add(report);
			}
		} finally {
			close(statement);
		}
		return reports;
	}

	public void update(ReportData reportData) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format(
					"UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s' AND %s='%s' AND %s='%s'",
					tableName, //
					Fields.GAME_NAME, reportData.getGameName(), //
					Fields.GAMERTAG, reportData.getGamerTag(), //
					Fields.PLATFORM, reportData.getPlatform(), //
					Fields.WIN, reportData.getWin(), Fields.LOSS, reportData.getLoss(), Fields.GAME_NAME,
					reportData.getGameName(), //
					Fields.GAMERTAG, reportData.getGamerTag(), //
					Fields.PLATFORM, reportData.getPlatform());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void delete(ReportData reportData) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s' AND %s='%s' AND %s='%s'", tableName,
					Fields.GAME_NAME, reportData.getGameName(), Fields.GAMERTAG, reportData.getGamerTag(),
					Fields.PLATFORM, reportData.getPlatform());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void init() throws ApplicationException {
		try {
			if (!Database.tableExists(ReportDao.TABLE_NAME)) {
				File personaFile = new File(PERSONAS_DATA_FILENAME);
				File gameFile = new File(GAMES_DATA_FILENAME);
				File scoreFile = new File(SCORES_DATA_FILENAME);
				if (!scoreFile.exists() || !personaFile.exists() || !gameFile.exists()) {
					LOG.error(String.format("Required '%s' is missing.", "File(s)"));
					System.exit(-1);
				}
				int reportCount = 0;
				create();
				List<Persona> personas = PersonaReader.read(personaFile);
				List<Game> games = GameReader.read(gameFile);
				List<Score> scores = ScoreReader.read(scoreFile);

				List<ReportData> reports = ReportReader.read(scores, personas, games);
				for (ReportData report : reports) {
					add(report);
					reportCount++;
				}

				LOG.debug("Inserted " + reportCount + " reports");
			}

		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public enum Fields {

		GAME_NAME(1), GAMERTAG(2), PLATFORM(3), WIN(4), LOSS(5);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}

}
