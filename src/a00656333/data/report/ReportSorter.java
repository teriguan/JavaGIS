/**
 * Project: A00656333Gis
 * File: GameReportSort.java
 * Date: Jun 23, 2016
 * Time: 12:57:10 AM
 */

package a00656333.data.report;

import java.util.Comparator;

/**
 * Sorts the game report data for printing.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class ReportSorter {

	public static class CompareByGamerTag implements Comparator<ReportData> {

		/**
		 * Compares gamertag.
		 */
		@Override
		public int compare(ReportData game1, ReportData game2) {
			return game1.getGamerTag().compareTo(game2.getGamerTag());
		}
	}

	public static class CompareByGame implements Comparator<ReportData> {

		/**
		 * Compares game name.
		 */
		@Override
		public int compare(ReportData game1, ReportData game2) {
			return game1.getGameName().compareTo(game2.getGameName());
		}
	}

	public static class CompareByCount implements Comparator<ReportData> {

		/**
		 * Compares game play count.
		 */
		@Override
		public int compare(ReportData game1, ReportData game2) {
			int result;
			int game1Total = game1.getWin() + game1.getLoss();
			int game2Total = game2.getWin() + game2.getLoss();

			if (game1Total == game2Total) {
				result = game1.getWin() - game2.getWin();
			} else {
				result = game1Total - game2Total;
			}

			return result;
		}
	}

	public static class CompareByGameDesc implements Comparator<ReportData> {

		/**
		 * Compares game name in descending order.
		 */
		@Override
		public int compare(ReportData game1, ReportData game2) {
			return game2.getGameName().compareTo(game1.getGameName());
		}
	}

	public static class CompareByCountDesc implements Comparator<ReportData> {

		/**
		 * Compares game play count.
		 */
		@Override
		public int compare(ReportData game1, ReportData game2) {
			int result;
			int game1Total = game1.getWin() + game1.getLoss();
			int game2Total = game2.getWin() + game2.getLoss();

			if (game1Total == game2Total) {
				result = game2.getWin() - game1.getWin();
			} else {
				result = game2Total - game1Total;
			}

			return result;
		}
	}

}
