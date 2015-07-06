/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.parsing.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;

import engine.parsing.TokenReader;

/**
 * A string in a JSON file.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class JSONString extends JSONValue {
	/**
	 * Parses a value from a token source.
	 * 
	 * @param tokens
	 *            The tokens to parse
	 * @param currentToken
	 *            The current token of interest.
	 * @return A JSONValue parsed from the tokens
	 * @throws IOException
	 *             If a token cannot be read
	 * @throws ParseException
	 *             If the tokens cannot be parsed into a JSONValue.
	 */
	public static JSONValue parse(TokenReader tokens, String currentToken)
			throws IOException, ParseException {
		return new JSONString(currentToken.substring(1,
				currentToken.length() - 1));
	}

	private String value;

	/**
	 * Creates a JSONString from a Java String
	 * @param value the String to create from.
	 */
	public JSONString(String value) {
		if (value == null) {
			throw new NullPointerException("String cannot have a null value");
		}
		this.value = value;
	}

	/**
	 * Creates a JSONString from a Java character.
	 * @param value The character to create from.
	 */
	public JSONString(char value) {
		this(Character.toString(value));
	}

	@Override
	public boolean isString() {
		return true;
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSONString other = (JSONString) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write('\"' + value + '\"');
	}
}
