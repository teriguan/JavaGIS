/**
 * Project: A00656333Gis
 * File: PlayerDao.java
 * Date: Jul 14, 2016
 * Time: 10:06:04 PM
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
import a00656333.data.player.Player;
import a00656333.data.player.io.PlayerReader;

/**
 * Creates Player database and populates with player attributes.
 * 
 * @author Teresa Guan, A00656333
 * @author scirka
 *
 */

public class PlayerDao extends Dao {

	private static final String PLAYERS_DATA_FILENAME = "players.dat";

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	public static final String TABLE_NAME = "Player";

	public PlayerDao() {
		super(Database.getInstance(), TABLE_NAME);
	}

	@Override
	public void create() throws SQLException {
		String createStatement = String.format(
				"create table %s(%s VARCHAR(9), %s VARCHAR(15), %s VARCHAR(15), %s VARCHAR(30), %s VARCHAR(8), primary key (%s) )",
				tableName, //
				Fields.ID, Fields.FIRSTNAME, Fields.LASTNAME, Fields.EMAIL, Fields.BIRTHDATE, Fields.ID);
		super.create(createStatement);
	}

	public void add(Player player) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', '%s', '%s', '%s', '%s')", tableName, //
					"" + player.getId(), //
					player.getFirstName(), //
					player.getLastName(), //
					player.getEmailAddress(), //
					// player.getGamerTag(), //
					player.getBirthDateString());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	public List<String> getFullNames() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<String> fullNames = new ArrayList<String>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s ORDER BY %s", tableName, Fields.LASTNAME);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// Get the full names to a list
			while (resultSet.next()) {
				String fullName = String.format("%s %s", resultSet.getString(Fields.FIRSTNAME.name()),
						resultSet.getString(Fields.LASTNAME.name()));

				fullNames.add(fullName);
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s", tableName));
		LOG.debug(String.format("Loaded %d full names from database", fullNames.size()));
		return fullNames;
	}

	public Player getPlayer(int playerId) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Player player = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement(); // Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.ID, playerId);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Player
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				player = new Player();
				player.setId(resultSet.getInt(Fields.ID.name()));
				player.setFirstName(resultSet.getString(Fields.FIRSTNAME.name()));
				player.setLastName(resultSet.getString(Fields.LASTNAME.name()));
				player.setEmailAddress(resultSet.getString(Fields.EMAIL.name()));
				player.setBirthDate(resultSet.getString(Fields.BIRTHDATE.name()));
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.ID, playerId));
		LOG.debug(Instant.now());
		return player;
	}

	public void update(Player player) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Fields.ID, player.getId(), //
					Fields.FIRSTNAME, player.getFirstName(), //
					Fields.LASTNAME, player.getLastName(), //
					Fields.EMAIL, player.getEmailAddress(), //
					Fields.BIRTHDATE, player.getBirthDateString(), //
					Fields.ID, player.getId());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void delete(Player player) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.ID, player.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void init() throws ApplicationException {
		try {
			if (!Database.tableExists(PlayerDao.TABLE_NAME)) {
				File file = new File(PLAYERS_DATA_FILENAME);
				if (!file.exists()) {
					LOG.error(String.format("Required '%s' is missing.", PLAYERS_DATA_FILENAME));
					System.exit(-1);
				}

				int playerCount = 0;
				create();
				List<Player> players = PlayerReader.read(file);
				for (Player player : players) {
					add(player);
					playerCount++;
				}

				LOG.debug("Inserted " + playerCount + " players");
			}
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public enum Fields {

		ID(1), FIRSTNAME(2), LASTNAME(3), EMAIL(4), BIRTHDATE(5);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}

}
