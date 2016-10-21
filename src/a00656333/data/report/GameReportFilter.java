/**
 * Project: A00656333Gis
 * File: GameReportFilter.java
 * Date: Jun 23, 2016
 * Time: 10:55:38 AM
 */

package a00656333.data.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.ApplicationException;

/**
 * Filters report data according to criteria met.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class GameReportFilter {

	private static final Logger LOG = LogManager.getLogger(Gis.class);
	private static boolean total;

	/**
	 * Private constructor to prevent instantiation.
	 */
	private GameReportFilter() {

	}

	/**
	 * Filters and sorts game report according to user input.
	 * 
	 * @param reports
	 *            the report
	 * @param filters
	 *            the commandline arguments
	 * @return reports the updated game report.
	 * @throws ApplicationException
	 */
	public static List<ReportData> filterGameReport(List<ReportData> reports, String[] filters)
			throws ApplicationException {

		if (filters.length > 4) {
			throw new ApplicationException("Too many inputs");
		}
		boolean byCount = false;
		boolean byGame = false;
		boolean desc = false;
		int platformNum = 0;

		for (String arg : filters) {

			String filter = arg.toUpperCase();

			if (filter.matches("PLATFORM=.*") && platformNum == 0) {
				String platform = filter.substring(9);

				switch (platform) {
				case "AN":
					reports = platformFilter(reports, platform);
					break;
				case "IO":
					reports = platformFilter(reports, platform);
					break;
				case "PC":
					reports = platformFilter(reports, platform);
					break;
				case "PS":
					reports = platformFilter(reports, platform);
					break;
				case "XB":
					reports = platformFilter(reports, platform);
					break;
				default:
					throw new ApplicationException(String.format("\"%s\" not valid platform", platform));
				}

				if (platformNum > 0) {
					throw new ApplicationException("Too many platform inputs");
				}
				platformNum++;

			} else if (filter.equals("DESC")) {
				if (desc) {
					throw new ApplicationException("Too many DESC inputs");
				}
				desc = true;
			} else if (filter.equals("BY_COUNT")) {
				if (byCount) {
					throw new ApplicationException("Too many BY_COUNT inputs");
				}
				byCount = true;
			} else if (filter.equals("BY_GAME")) {
				if (byGame) {
					throw new ApplicationException("Too many BY_GAME inputs");
				}
				byGame = true;
			} else if (filter.equals("TOTAL")) {
				if (total) {
					throw new ApplicationException("Too many TOTAL inputs");
				}
				total = true;
			} else {
				throw new ApplicationException(String.format("\"%s\" not valid filter", filter));
			}
		}

		if (byCount && byGame) {
			throw new ApplicationException("Cannot sort by count and game.");
		} else if (byCount && desc) {
			Collections.sort(reports, new ReportSorter.CompareByCountDesc());
		} else if (byCount && !desc) {
			Collections.sort(reports, new ReportSorter.CompareByCount());
		} else if (byGame && desc) {
			Collections.sort(reports, new ReportSorter.CompareByGameDesc());
		} else if (byGame && !desc) {
			Collections.sort(reports, new ReportSorter.CompareByGame());
		} else {
			return reports;
		}

		return reports;
	}

	/**
	 * Filters out report data if it does not match platform.
	 * 
	 * @param reports
	 *            the game report data
	 * @param platform
	 *            the platform
	 */
	public static List<ReportData> platformFilter(List<ReportData> reports, String platform) {

		List<ReportData> newReports = new ArrayList<ReportData>();

		for (ReportData report : reports) {

			String reportPlatform = report.getPlatform();
			if (platform.equals(reportPlatform)) {
				newReports.add(report);
			}
		}

		LOG.info("Updated game report list");
		return newReports;

	}

	public static boolean isTotal() {
		return total;
	}

	public static void setTotal(boolean total) {
		GameReportFilter.total = total;
	}
}
