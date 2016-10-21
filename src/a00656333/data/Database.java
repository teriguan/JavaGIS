/**
 * Project: A00656333Gis
 * File: Database.java
 * Date: Jul 14, 2016
 * Time: 10:09:18 PM
 */

package a00656333.data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;

/**
 * Creates the database connection.
 * 
 * @author Teresa Guan, A00656333
 * @author scirka
 */

public class Database {

	public static final String DB_DRIVER_KEY = "db.driver";
	public static final String DB_URL_KEY = "db.url";
	public static final String DB_USER_KEY = "db.user";
	public static final String DB_PASSWORD_KEY = "db.password";

	private static Logger LOG = LogManager.getLogger(Gis.class);

	private static Database databaseInstance;

	private static Connection connection;
	private static Properties properties;

	private Database() {
	}

	public static Database getInstance() {
		if (databaseInstance == null) {
			synchronized (Database.class) {
				databaseInstance = new Database();
			}
		}

		return databaseInstance;
	}

	public void init(Properties properties) {
		if (Database.properties == null) {
			LOG.debug("Loading database properties from db.properties");
			Database.properties = properties;
		}
	}

	public Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}

		try {
			connect();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}

		return connection;
	}

	private void connect() throws ClassNotFoundException, SQLException {
		Class.forName(properties.getProperty(DB_DRIVER_KEY));
		LOG.debug("Driver loaded");
		connection = DriverManager.getConnection(properties.getProperty(DB_URL_KEY),
				properties.getProperty(DB_USER_KEY), properties.getProperty(DB_PASSWORD_KEY));
		LOG.debug("Database connected");
	}

	public void shutdown() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
				LOG.debug("Connection closed.");
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
	}

	public static boolean tableExists(String tableName) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = null;
		String rsTableName = null;

		try {
			resultSet = databaseMetaData.getTables(connection.getCatalog(), "%", "%", null);
			while (resultSet.next()) {
				rsTableName = resultSet.getString("TABLE_NAME");
				if (rsTableName.equalsIgnoreCase(tableName)) {
					return true;
				}
			}
		} finally {
			resultSet.close();
		}

		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Don't think about it!");
	}
}
