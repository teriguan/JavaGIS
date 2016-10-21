/**
 * Project: A00656333Gis
 * File: Player.java
 * Date: Jun 19, 2016
 * Time: 4:40:25 PM
 */

package a00656333.data.player;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;

/**
 * Creates Player objects.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class Player {

	private static final Logger LOG = LogManager.getLogger(Gis.class);

	public static final int FILE_ATTRIBUTE_COUNT = 5;
	private int id;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private LocalDate birthDate;
	private int gamesPlayed;
	private int wins;

	/**
	 * Default Constructor
	 */
	public Player() {
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the birthDate
	 */
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getBirthDateString() {
		if (birthDate == null) {
			return null;
		}

		return birthDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Set the birthdate
	 * 
	 * @param year
	 *            the year, includes the century, ex. 1967
	 * @param month
	 *            the month - must be 0-based
	 * @param day
	 *            the day of the month - 1-based
	 */
	public void setBirthDate(int year, int month, int day) {
		birthDate = LocalDate.of(year, month, day);
	}

	public void setBirthDate(String birthDate) {
		if (birthDate == null || birthDate.trim().length() == 0 || birthDate.equalsIgnoreCase("null")) {
			// don't set the birthdate
		} else {
			try {
				this.birthDate = LocalDate.of(Integer.parseInt(birthDate.substring(0, 4)),
						Integer.parseInt(birthDate.substring(4, 6)) - 1, Integer.parseInt(birthDate.substring(6, 8)));
			} catch (NumberFormatException e) {
				LOG.error("Invalid date element:" + birthDate);
			}
		}
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
}
