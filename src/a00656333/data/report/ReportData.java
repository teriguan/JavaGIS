/**
 * Project: A00656333Gis
 * File: GameReportObject.java
 * Date: Jun 21, 2016
 * Time: 7:16:16 PM
 */

package a00656333.data.report;

/**
 * @author Teresa Guan, A00656333
 *
 */

public class ReportData {

	public static final int FILE_ATTRIBUTE_COUNT = 3;
	private int win;
	private int loss;
	private String gameName;
	private String gamerTag;
	private String platform;

	/**
	 * Default constructor.
	 */
	public ReportData() {
	}

	/**
	 * @return the win
	 */
	public int getWin() {
		return win;
	}

	/**
	 * @param win
	 *            The win to set
	 */
	public void setWin(int win) {
		this.win = win;
	}

	/**
	 * @return the loss
	 */
	public int getLoss() {
		return loss;
	}

	/**
	 * @param loss
	 *            The loss to set
	 */
	public void setLoss(int loss) {
		this.loss = loss;
	}

	/**
	 * @return the game name
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName
	 *            The game name to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the gamer tag
	 */
	public String getGamerTag() {
		return gamerTag;
	}

	/**
	 * @param gamerTag
	 *            The gamer tag to set
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

	/**
	 * Returns unique hashcode.
	 * 
	 * @return hashcode as an int.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameName == null) ? 0 : gameName.hashCode());
		result = prime * result + ((gamerTag == null) ? 0 : gamerTag.hashCode());
		result = prime * result + ((platform == null) ? 0 : platform.hashCode());
		return result;
	}

	/**
	 * Returns true if two objects' game name, gamertag, and platform are the
	 * same.
	 * 
	 * @param obj
	 *            the other object
	 * @return boolean result
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportData other = (ReportData) obj;
		if (gameName == null) {
			if (other.gameName != null)
				return false;
		} else if (!gameName.equals(other.gameName))
			return false;
		if (gamerTag == null) {
			if (other.gamerTag != null)
				return false;
		} else if (!gamerTag.equals(other.gamerTag))
			return false;
		if (platform == null) {
			if (other.platform != null)
				return false;
		} else if (!platform.equals(other.platform))
			return false;
		return true;
	}

}
