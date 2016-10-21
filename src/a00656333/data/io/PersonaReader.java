/**
 * Project: A00656333Gis
 * File: PersonaReader.java
 * Date: Jun 20, 2016
 * Time: 2:02:36 AM
 */

package a00656333.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00656333.Gis;
import a00656333.data.ApplicationException;
import a00656333.data.Persona;

/**
 * @author Teresa Guan, A00656333
 *
 */

public class PersonaReader {

	private static final Logger LOG = LogManager.getLogger(Gis.class);
	private static final String ATTRIBUTE_DELIMITER = "\\|";

	/**
	 * Read the persona data from a file.
	 * 
	 * @param personaData
	 *            The data from file
	 * @return A List of Personas
	 * @throws ApplicationException
	 */
	public static List<Persona> read(File file) throws ApplicationException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}

		List<Persona> personas = new ArrayList<Persona>();

		try {
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String[] elements = scanner.nextLine().split(ATTRIBUTE_DELIMITER);
				if (elements.length == Persona.FILE_ATTRIBUTE_COUNT) {
					Persona persona = new Persona();
					int index = 0;
					persona.setId(Integer.parseInt(elements[index++]));
					persona.setPlayerId(Integer.parseInt(elements[index++]));
					persona.setGamerTag(elements[index++]);
					persona.setPlatform(elements[index++]);
					personas.add(persona);
				} else
					throw new ApplicationException(String.format("Received %d out of %d expected elements.",
							elements.length, Persona.FILE_ATTRIBUTE_COUNT));
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		LOG.debug("Persona items: " + personas.size());
		return personas;
	}

}
