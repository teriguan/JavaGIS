/**
 * Project: A00656333Gis
 * File: Score.java
 * Date: Jun 19, 2016
 * Time: 4:41:27 PM
 */

package a00656333.data;

/**
 * Creates Score objects.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class Score {
	public static final int FILE_ATTRIBUTE_COUNT = 3;

	public static final String WIN = "WIN";
	public static final String LOSS = "LOSS";

	private int personaId;
	private String gameId;
	private boolean win;

	private String personaName;
	private String gameName;

	/**
	 * Default constructor.
	 */
	public Score() {
	}

	/**
	 * @return the persona id
	 */
	public int getPersonaId() {
		return personaId;
	}

	/**
	 * @param personaId
	 *            the persona id to set
	 */
	public void setPersonaId(int personaId) {
		this.personaId = personaId;
	}

	/**
	 * @return the game id
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @param gameId
	 *            the game id to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the win
	 */
	public boolean getWin() {
		return win;
	}

	/**
	 * @return the win as a string
	 */
	public String getWinString() {
		if (this.win) {
			return WIN;
		} else {
			return LOSS;
		}
	}

	/**
	 * @param win
	 *            the win to set
	 */
	public void setWin(boolean win) {
		this.win = win;
	}

	public void setWin(String win) throws ApplicationException {
		if (win.equalsIgnoreCase(WIN)) {
			this.win = true;
		} else if (win.equalsIgnoreCase(LOSS)) {
			this.win = false;
		} else {
			throw new ApplicationException(String.format("%s is not a valid game outcome.", win));
		}
	}

	public String getPersonaName() {
		return personaName;
	}

	public void setPersonaName(String personaName) {
		this.personaName = personaName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

}
