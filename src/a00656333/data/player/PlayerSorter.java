/**
 * Project: A00656333Gis
 * File: PlayerSorter.java
 * Date: Jun 20, 2016
 * Time: 1:37:12 AM
 */

package a00656333.data.player;

import java.time.Period;
import java.util.Comparator;

/**
 * @author Teresa Guan, A00656333
 *
 */

public class PlayerSorter {

	public static class CompareByBirthdate implements Comparator<Player> {

		/**
		 * Compares birthdates between two players.
		 */
		@Override
		public int compare(Player player1, Player player2) {

			return Period.between(player1.getBirthDate(), player2.getBirthDate()).getDays();
		}

	}

	public static class CompareByLastName implements Comparator<Player> {

		/**
		 * Compares birthdates between two players.
		 */
		@Override
		public int compare(Player player1, Player player2) {

			return player1.getLastName().compareTo(player2.getLastName());
		}

	}

}
