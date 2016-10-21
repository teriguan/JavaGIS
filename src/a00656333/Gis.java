/**
 * Project: A00656333Gis
 * File: Gis.java
 * Date: Jun 19, 2016
 * Time: 4:40:08 PM
 */

package a00656333;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.dao.GameDao;
import a00656333.dao.PersonaDao;
import a00656333.dao.PlayerDao;
import a00656333.dao.ReportDao;
import a00656333.dao.ScoreDao;
import a00656333.data.ApplicationException;
import a00656333.data.Database;
import a00656333.ui.MainFrame;

/**
 * Demostrates the usage of knowledge learn from previous labs to create the
 * core of a Games Information System(GIS). Local database, multithreading, GUI,
 * model-view-controller and network programming were used to enhance the user
 * experience of the previous assignment.
 * 
 * This program makes use of packages, the Scanner class, Strings, regular
 * expressions, formatting output, object design, jar files, annotations,
 * exceptions, date & time formatting, Generics, Collections, File IO, (log4j)
 * logging and MVC.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class Gis {

	public static final String DB_PROPERTIES_FILENAME = "db.properties";

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	private static Database database;
	private static PlayerDao playerDao;
	private static PersonaDao personaDao;
	private static GameDao gameDao;
	private static ScoreDao scoreDao;
	private static ReportDao reportDao;

	private static Instant start;
	private static Instant end;
	private static Duration programRun;

	/**
	 * Drives the program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		setStartTime();

		try {

			LOG.info("Created Gis");

			new Gis().run();

		} catch (ApplicationException e) {
			LOG.error(e.toString());
		} catch (FileNotFoundException f) {
			LOG.error(f.getMessage());
		} catch (IOException i) {
			LOG.error(i.getMessage());
		} catch (Exception o) {
			LOG.error(o.getMessage());
		} finally {
			database.shutdown();
			setEndTime();
			duration();
		}
	}

	/**
	 * Gis constructor.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Gis() throws FileNotFoundException, IOException {
		Properties dbProperties = new Properties();
		dbProperties.load(new FileInputStream(DB_PROPERTIES_FILENAME));

		database = Database.getInstance();
		database.init(dbProperties);
	}

	/**
	 * Populates the players, personas, games and scores.
	 * 
	 * @throws ApplicationException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	private void run() throws ApplicationException, FileNotFoundException, IOException, SQLException {
		database.getConnection();
		init();
		createUI();
	}

	private void init() throws IOException, ApplicationException {

		playerDao = new PlayerDao();
		playerDao.init();
		personaDao = new PersonaDao();
		personaDao.init();
		gameDao = new GameDao();
		gameDao.init();
		scoreDao = new ScoreDao();
		scoreDao.init();
		reportDao = new ReportDao();
		reportDao.init();
	}

	private void setLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, use the default.
		}
	}

	public void createUI() {
		setLookAndFeel();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame mainFrame = new MainFrame(playerDao, personaDao, gameDao, scoreDao, reportDao);
					mainFrame.setVisible(true);
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		});
	}

	/**
	 * Sets the time of when the program starts and prints it out.
	 */
	private static void setStartTime() {
		start = Instant.now();
		LOG.info("Program Start: " + start);
	}

	/**
	 * Sets the time of when the program ends and prints in out.
	 */
	private static void setEndTime() {
		end = Instant.now();
		LOG.info("Program End: " + end);
	}

	/**
	 * Calculates the runtime of the program and prints it out.
	 */
	private static void duration() {
		programRun = Duration.between(start, end);
		LOG.info("Duration: " + programRun.toMillis() + " ms");
	}

}
