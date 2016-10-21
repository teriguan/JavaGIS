/**
 * Project: A00656333Gis
 * File: PersonaDao.java
 * Date: Jul 14, 2016
 * Time: 10:06:47 PM
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
import a00656333.data.Persona;
import a00656333.data.io.PersonaReader;
import a00656333.data.player.Player;

/**
 * Creates Persona table and populates with persona attributes.
 * 
 * @author Teresa Guan, A00656333
 * @author scirka
 *
 */

public class PersonaDao extends Dao {

	private static final String PERSONAS_DATA_FILENAME = "personas.dat";

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	public static final String TABLE_NAME = "Persona";

	public PersonaDao() {
		super(Database.getInstance(), TABLE_NAME);
	}

	@Override
	public void create() throws SQLException {
		String createStatement = String.format(
				"create table %s(%s VARCHAR(5), %s VARCHAR(5), %s VARCHAR(20), %s VARCHAR(3), primary key (%s) )",
				tableName, //
				Fields.ID, Fields.PLAYER_ID, Fields.GAMERTAG, Fields.PLATFORM, Fields.ID);
		super.create(createStatement);
	}

	public void add(Persona persona) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', '%s', '%s', '%s')", tableName, //
					"" + persona.getId(), //
					persona.getPlayerId(), //
					persona.getGamerTag(), //
					persona.getPlatform());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	public List<String> getGamerTags() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<String> gamerTags = new ArrayList<String>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s ORDER BY %s", tableName, Fields.GAMERTAG);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// Get the gamertags to a list
			while (resultSet.next()) {
				String gamerTag = resultSet.getString(Fields.GAMERTAG.name());

				gamerTags.add(gamerTag);
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s", tableName));
		LOG.debug(String.format("Loaded %d gamertags from database", gamerTags.size()));
		return gamerTags;
	}

	public Persona getPersona(String gamerTag) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Persona persona = null;
		// List<Score> scores = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement(); // Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.GAMERTAG, gamerTag);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Persona
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				persona = new Persona();
				persona.setId(resultSet.getInt(Fields.ID.name()));
				persona.setPlayerId(resultSet.getInt(Fields.PLAYER_ID.name()));
				persona.setGamerTag(resultSet.getString(Fields.GAMERTAG.name()));
				persona.setPlatform(resultSet.getString(Fields.PLATFORM.name()));
				/*
				 * String scoreSqlString = String.format(
				 * "SELECT * FROM %s WHERE %s = '%s'", ScoreDao.TABLE_NAME,
				 * ScoreDao.Fields.PERSONA_ID, persona.getId()); ResultSet
				 * scoreResultSet = statement.executeQuery(scoreSqlString);
				 * scores = new ArrayList<Score>(); Score score = null; while
				 * (scoreResultSet.next()) { score = new Score();
				 * score.setPersonaId(scoreResultSet.getInt(ScoreDao.Fields.
				 * PERSONA_ID.name()));
				 * score.setGameId(scoreResultSet.getString(ScoreDao.Fields.
				 * GAME_ID.name()));
				 * score.setWin(scoreResultSet.getString(ScoreDao.Fields.WIN.
				 * name())); scores.add(score); }
				 */
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.ID, persona.getId()));
		LOG.debug(Instant.now());
		return persona;
	}

	public Player getPlayer(Persona persona) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Player player = null;

		try {
			connection = database.getConnection();
			statement = connection.createStatement(); // Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", PlayerDao.TABLE_NAME,
					PlayerDao.Fields.ID, persona.getPlayerId());
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Player // throw an exception if we get more than one
			// result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				player = new Player();
				player.setId(resultSet.getInt(PlayerDao.Fields.ID.name()));
				player.setFirstName(resultSet.getString(PlayerDao.Fields.FIRSTNAME.name()));
				player.setLastName(resultSet.getString(PlayerDao.Fields.LASTNAME.name()));
				player.setEmailAddress(resultSet.getString(PlayerDao.Fields.EMAIL.name()));
				player.setBirthDate(resultSet.getString(PlayerDao.Fields.BIRTHDATE.name()));
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.ID, persona.getPlayerId()));
		LOG.debug(Instant.now());
		return player;
	}

	public void update(Persona persona) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Fields.ID, persona.getId(), //
					Fields.PLAYER_ID, persona.getPlayerId(), //
					Fields.GAMERTAG, persona.getGamerTag(), //
					Fields.PLATFORM, persona.getPlatform(), //
					Fields.ID, persona.getId());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void delete(Persona persona) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.ID, persona.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void init() throws ApplicationException {
		try {
			if (!Database.tableExists(PersonaDao.TABLE_NAME)) {
				File file = new File(PERSONAS_DATA_FILENAME);
				if (!file.exists()) {
					LOG.error(String.format("Required '%s' is missing.", PERSONAS_DATA_FILENAME));
					System.exit(-1);
				}

				int personaCount = 0;
				create();
				List<Persona> personas = PersonaReader.read(file);
				for (Persona persona : personas) {
					add(persona);
					personaCount++;
				}

				LOG.debug("Inserted " + personaCount + " personas");
			}
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public enum Fields {

		ID(1), PLAYER_ID(2), GAMERTAG(3), PLATFORM(4);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}

}
