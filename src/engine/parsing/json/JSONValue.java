/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.parsing.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import engine.parsing.TokenReader;

/**
 * Represents a value in a JSON file.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public abstract class JSONValue {
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
		int c = currentToken.charAt(0);
		if (TokenReader.isNumber(c)) {
			return JSONNumber.parse(tokens, currentToken);
		}

		switch (c) {
		case 'n':
		case 't':
		case 'f':
			return JSONLiteral.parse(tokens, currentToken);
		case '"':
			return JSONString.parse(tokens, currentToken);
		case '[':
			return JSONArray.parse(tokens, currentToken);
		case '{':
			return JSONObject.parse(tokens, currentToken);
		}

		throw new IOException(currentToken + " is not a recognized JSON value");
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(int value) {
		return new JSONNumber(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(long value) {
		return new JSONNumber(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(float value) {
		return new JSONNumber(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(double value) {
		return new JSONNumber(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(short value) {
		return new JSONNumber(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(byte value) {
		return new JSONNumber(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(char value) {
		return new JSONString(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(boolean value) {
		return JSONLiteral.create(value);
	}

	/**
	 * Creates a JSONValue from a Java type.
	 * 
	 * @param value
	 *            The value to turn into a JSONValue
	 * @return The JSONValue equivalent of value.
	 */
	public static JSONValue create(String value) {
		if (value == null) {
			return JSONLiteral.NULL;
		} else {
			return new JSONString(value);
		}
	}

	/**
	 * Writes this JSONValue with a writer.
	 * 
	 * @param writer
	 *            The Writer to write with.
	 * @throws IOException
	 *             If the value cannot be written.
	 */
	public abstract void write(Writer writer) throws IOException;

	/**
	 * Gets if this is a JSONObject.
	 * 
	 * @return True if this is a JSONObject, false otherwise.
	 */
	public boolean isObject() {
		return false;
	}

	/**
	 * Gets if this is a JSONArray.
	 * 
	 * @return True if this is a JSONArray, false otherwise.
	 */
	public boolean isArray() {
		return false;
	}

	/**
	 * Gets if this is a JSONString.
	 * 
	 * @return True if this is a JSONString, false otherwise.
	 */
	public boolean isString() {
		return false;
	}

	/**
	 * Gets if this is a JSONNumber.
	 * 
	 * @return True if this is a JSONNumber, false otherwise.
	 */
	public boolean isNumber() {
		return false;
	}

	/**
	 * Gets if this is a boolean JSON value.
	 * 
	 * @return True if this is a boolean JSON value, false otherwise.
	 */
	public boolean isBoolean() {
		return false;
	}

	/**
	 * Gets if this is a null JSON value.
	 * 
	 * @return True if this is a null JSON value, false otherwise.
	 */
	public boolean isNull() {
		return false;
	}

	/**
	 * Interprets this as an object.
	 * 
	 * @return The value of this as an object.
	 */
	public Map<String, JSONValue> asObject() {
		throw new UnsupportedOperationException("Not an object: " + toString());
	}

	/**
	 * Interprets this as an array.
	 * 
	 * @return The value of this as an array.
	 */
	public List<JSONValue> asArray() {
		throw new UnsupportedOperationException("Not an array: " + toString());
	}

	/**
	 * Interprets this as a String.
	 * 
	 * @return The value of this as a String.
	 */
	public String asString() {
		throw new UnsupportedOperationException("Not a string: " + toString());
	}

	/**
	 * Interprets this as an int.
	 * 
	 * @return The value of this as an int.
	 */
	public int asInt() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	/**
	 * Interprets this as a long.
	 * 
	 * @return The value of this as a long.
	 */
	public long asLong() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	/**
	 * Interprets this as a float.
	 * 
	 * @return The value of this as a float.
	 */
	public float asFloat() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	/**
	 * Interprets this as a double.
	 * 
	 * @return The value of this as a double.
	 */
	public double asDouble() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	/**
	 * Interprets this as a boolean.
	 * 
	 * @return The value of this as a boolean.
	 */
	public boolean asBoolean() {
		throw new UnsupportedOperationException("Not a boolean: " + toString());
	}
}
