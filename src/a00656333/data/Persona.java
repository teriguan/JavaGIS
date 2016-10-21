/**
 * Project: A00656333Gis
 * File: Persona.java
 * Date: Jun 19, 2016
 * Time: 4:41:08 PM
 */

package a00656333.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates Persona object.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class Persona {
	public static final int FILE_ATTRIBUTE_COUNT = 4;
	private int id;
	private int playerId;
	private String gamerTag;
	private String platform;

	private List<Score> gamesPlayed;

	/**
	 * Default constructor.
	 */
	public Persona() {
		gamesPlayed = new ArrayList<Score>();
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
	 * @return the player id
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 *            the player id to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the gamertag
	 */
	public String getGamerTag() {
		return gamerTag;
	}

	/**
	 * @param gamerTag
	 *            the gamertag to set
	 */
	public void setGamerTag(String gamerTag) {
		this.gamerTag = gamerTag;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void addScore(Score score) {
		gamesPlayed.add(score);
	}

	public List<Score> getGamesPlayed() {
		return gamesPlayed;
	}

}
