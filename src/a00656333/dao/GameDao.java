/**
 * Project: A00656333Gis
 * File: GameDao.java
 * Date: Jul 14, 2016
 * Time: 10:06:29 PM
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
import a00656333.data.Game;
import a00656333.data.io.GameReader;

/**
 * Creates Game table and populates with player attributes.
 * 
 * @author Teresa Guan, A00656333
 * @author scirka
 *
 */

public class GameDao extends Dao {

	private static final String GAMES_DATA_FILENAME = "games.dat";

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	public static final String TABLE_NAME = "Game";

	public GameDao() {
		super(Database.getInstance(), TABLE_NAME);
	}

	@Override
	public void create() throws SQLException {
		String createStatement = String.format(
				"create table %s(%s VARCHAR(4), %s VARCHAR(20), %s VARCHAR(20), primary key (%s) )", tableName, //
				Fields.ID, Fields.NAME, Fields.PRODUCER, Fields.ID);
		super.create(createStatement);
	}

	public void add(Game game) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String insertString = String.format("insert into %s values('%s', '%s', '%s')", tableName, //
					"" + game.getId(), //
					game.getName(), //
					game.getProducer());
			statement.executeUpdate(insertString);
			LOG.debug(insertString);
		} finally {
			close(statement);
		}
	}

	public List<String> getGameIds() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<String> gameIds = new ArrayList<String>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// Get the game ids to a list
			while (resultSet.next()) {
				String gameId = resultSet.getString(Fields.ID.name());

				gameIds.add(gameId);
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s", tableName));
		LOG.debug(String.format("Loaded %d gamertags from database", gameIds.size()));
		return gameIds;
	}

	public Game getGame(String gameId) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Game game = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.ID, gameId);
			ResultSet resultSet = statement.executeQuery(sqlString);

			// get the Game
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				game = new Game();
				game.setId(resultSet.getString(Fields.ID.name()));
				game.setName(resultSet.getString(Fields.NAME.name()));
				game.setProducer(resultSet.getString(Fields.PRODUCER.name()));
			}
		} finally {
			close(statement);
		}
		LOG.debug(String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.ID, gameId));
		LOG.debug(Instant.now());
		return game;
	}

	public void update(Game game) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s' WHERE %s='%s'", tableName, //
					Fields.ID, game.getId(), //
					Fields.NAME, game.getName(), //
					Fields.PRODUCER, game.getProducer(), //
					Fields.ID, game.getId());
			System.out.println(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void delete(Game game) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.ID, game.getId());
			LOG.debug(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void init() throws ApplicationException {
		try {
			if (!Database.tableExists(GameDao.TABLE_NAME)) {
				File file = new File(GAMES_DATA_FILENAME);
				if (!file.exists()) {
					LOG.error(String.format("Required '%s' is missing.", GAMES_DATA_FILENAME));
					System.exit(-1);
				}

				int gameCount = 0;
				create();
				List<Game> games = GameReader.read(file);
				for (Game game : games) {
					add(game);
					gameCount++;
				}

				LOG.debug("Inserted " + gameCount + " games");
			}
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public enum Fields {

		ID(1), NAME(2), PRODUCER(3), PLAYS(4);

		private final int column;

		Fields(int column) {
			this.column = column;
		}

		public int getColumn() {
			return column;
		}
	}

}