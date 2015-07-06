/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Loads and parses a configuration file.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Config {
	private Map<String, String> map;

	/**
	 * Loads and parses a configuration file.
	 * 
	 * @param fileName
	 *            The name and path to the configuration file.
	 * @throws FileNotFoundException
	 *             If the file cannot be found.
	 * @throws IOException
	 *             If the file cannot be loaded.
	 * @throws ParseException
	 *             If the file cannot be properly parsed.
	 */
	public Config(String fileName) throws FileNotFoundException, IOException,
			ParseException {
		map = new HashMap<String, String>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				if (line.isEmpty()) {
					continue;
				}

				char start = line.charAt(0);
				if (start == '[' || start == '#') {
					continue;
				}
				String[] tokens = line.split("=");
				if (tokens.length != 2) {
					throw new ParseException("Line has too many '=' (line "
							+ lineNumber + ")", lineNumber);
				}

				map.put(tokens[0].trim(), tokens[1].trim());
			}
		}
	}

	/**
	 * Creates a representation of a configuration file.
	 * 
	 * @param map
	 *            The map of values in the configuration file.
	 */
	public Config(Map<String, String> map) {
		this.map = map;
	}

	/**
	 * Saves a new configuration file
	 * 
	 * @param fileName
	 *            The name and path of the output file.
	 * @throws IOException
	 *             If the file cannot be written.
	 */
	public void write(String fileName) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> pair = it.next();
				String line = pair.getKey() + "=" + pair.getValue() + "\n";
				bw.write(line);
			}
		}
	}

	/**
	 * Get a string from the configuration file.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @return The string assigned to that entry.
	 */
	public String getString(String entry) {
		String result = map.get(entry);
		if (result != null && result.charAt(0) == '$') {
			return getString(result.substring(1));
		}
		return result;
	}

	/**
	 * Get an integer from the configuration file.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @return The integer assigned to that entry.
	 */
	public int getInt(String entry) {
		return Integer.parseInt(getString(entry));
	}

	/**
	 * Get a double from the configuration file.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @return The double assigned to that entry.
	 */
	public double getDouble(String entry) {
		return Double.parseDouble(getString(entry));
	}

	/**
	 * Get a boolean from the configuration file.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @return The boolean assigned to that entry.
	 */
	public boolean getBoolean(String entry) {
		return Boolean.parseBoolean(getString(entry));
	}

	/**
	 * Get a string from the configuration file, with a default if that string
	 * cannot be found.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @param defaultEntry
	 *            The name of the default configuration entry.
	 * @return The string assigned to the entry if found, otherwise the string
	 *         assigned to the default entry.
	 */
	public String getStringWithDefault(String entry, String defaultEntry) {
		String result = getString(entry);
		if (result == null) {
			result = getString(defaultEntry);
		}
		return result;
	}

	/**
	 * Get an integer from the configuration file, with a default if that integer
	 * cannot be found.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @param defaultEntry
	 *            The name of the default configuration entry.
	 * @return The integer assigned to the entry if found, otherwise the integer
	 *         assigned to the default entry.
	 */
	public int getIntWithDefault(String entry, String defaultEntry) {
		return Integer.parseInt(getStringWithDefault(entry, defaultEntry));
	}

	/**
	 * Get a double from the configuration file, with a default if that double
	 * cannot be found.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @param defaultEntry
	 *            The name of the default configuration entry.
	 * @return The double assigned to the entry if found, otherwise the double
	 *         assigned to the default entry.
	 */
	public double getDoubleWithDefault(String entry, String defaultEntry) {
		return Double.parseDouble(getStringWithDefault(entry, defaultEntry));
	}

	/**
	 * Get a boolean from the configuration file, with a default if that boolean
	 * cannot be found.
	 * 
	 * @param entry
	 *            The name of the configuration entry.
	 * @param defaultEntry
	 *            The name of the default configuration entry.
	 * @return The boolean assigned to the entry if found, otherwise the boolean
	 *         assigned to the default entry.
	 */
	public boolean getBooleanWithDefault(String entry, String defaultEntry) {
		return Boolean.parseBoolean(getStringWithDefault(entry, defaultEntry));
	}
}
