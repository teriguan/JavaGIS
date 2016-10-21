/**
 * Project: A00656333Gis
 * File: Validator.java
 * Date: Jun 19, 2016
 * Time: 5:07:45 PM
 */

package a00656333.data.util;

import java.util.regex.Pattern;

/**
 * Validate data.
 * 
 * @author Teresa Guan, A00656333
 *
 */

public class Validator {

	/**
	 * Regex taken from cheat sheet.
	 */
	private static String REGEX = "(\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,6})";
	private static Pattern EMAIL_PATTERN = Pattern.compile(REGEX);

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Validator() {
	}

	/**
	 * Validates a player's email and returns the email if it is valid and N/A
	 * if it is not valid.
	 * 
	 * @param email
	 *            The input email string.
	 * @return true if the email address is valid, false otherwise.
	 */
	public static boolean validateEmail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

}