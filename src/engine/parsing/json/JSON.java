/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.parsing.json;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import engine.parsing.TokenReader;

/**
 * Reads or writes JSON files.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class JSON {
	private JSONValue value;

	/**
	 * Loads a JSON file
	 * 
	 * @param fileName
	 *            The name and path to the file of interest.
	 * @throws IOException
	 *             If the file cannot be loaded.
	 * @throws ParseException
	 *             If the file cannot be properly parsed.
	 */
	public JSON(String fileName) throws IOException, ParseException {
		TokenReader tokens;
		tokens = new TokenReader(new FileReader(fileName));
		value = JSONValue.parse(tokens, tokens.next());

		String token;
		tokens.parseAssert((token = tokens.next()) == null,
				"Expected EOF; instead got " + token);

		tokens.close();
	}

	/**
	 * Wraps a JSON value for writing.
	 * 
	 * @param value
	 *            The value to write to file.
	 */
	public JSON(JSONValue value) {
		this.value = value;
	}

	/**
	 * Writes a new JSON file.
	 * 
	 * @param fileName
	 *            The name and path to the file to write to.
	 * @throws IOException
	 *             If the file cannot be written.
	 */
	public void write(String fileName) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
		value.write(br);
		br.close();
	}

	/**
	 * Gets the JSONValue for this object
	 * 
	 * @return The JSONValue for this object.
	 */
	public JSONValue get() {
		return value;
	}
}
