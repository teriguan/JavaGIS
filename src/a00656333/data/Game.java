/**
 * Project: A00656333Gis
 * File: Game.java
 * Date: Jun 19, 2016
 * Time: 4:41:18 PM
 */

package a00656333.data;

/**
 * Creates Game objects.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class Game {
	public static final int FILE_ATTRIBUTE_COUNT = 3;
	private String id;
	private String name;
	private String producer;
	private int plays;

	/**
	 * Default constructor.
	 */
	public Game() {
	}

	/**
	 * @return the game id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the game id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the game name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the game name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the game producer
	 */
	public String getProducer() {
		return producer;
	}

	/**
	 * @param producer
	 *            the game producer to set
	 */
	public void setProducer(String producer) {
		this.producer = producer;
	}

	/**
	 * @return the game plays
	 */
	public int getPlays() {
		return plays;
	}

	/**
	 * @param plays
	 *            the game plays to set
	 */
	public void setPlays(int plays) {
		this.plays = plays;
	}

}
