/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.parsing.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import engine.parsing.TokenReader;

/**
 * An Object in a JSON file.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class JSONObject extends JSONValue {
	/**
	 * Parses a value from a token source.
	 * 
	 * @param tokens
	 *            The tokens to parse
	 * @param token
	 *            The current token of interest.
	 * @return A JSONValue parsed from the tokens
	 * @throws IOException
	 *             If a token cannot be read
	 * @throws ParseException
	 *             If the tokens cannot be parsed into a JSONValue.
	 */
	public static JSONValue parse(TokenReader tokens, String token)
			throws IOException, ParseException {
		JSONObject result = new JSONObject();

		if ((token = tokens.next()).equals("}")) {
			return result;
		}

		do {
			String key = JSONString.parse(tokens, token).toString();
			tokens.parseAssert((token = tokens.next()).equals(":"),
					"Separating ':' expected!");
			result.put(key, JSONValue.parse(tokens, tokens.next()));
			if (!(token = tokens.next()).equals(",")) {
				break;
			}
			token = tokens.next();
		} while (true);

		tokens.parseAssert(token.equals("}"), "Closing '}' expected!");
		return result;
	}

	private Map<String, JSONValue> map;

	/** Creates a new JSONObject */
	public JSONObject() {
		map = new HashMap<String, JSONValue>();
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, int value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, byte value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, short value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, long value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, float value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, double value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, boolean value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, char value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, String value) {
		put(key, JSONValue.create(value));
	}

	/**
	 * Adds a new value to the JSONObject.
	 * 
	 * @param key
	 *            The name for the value.
	 * @param value
	 *            The actual value.
	 */
	public void put(String key, JSONValue value) {
		if (key == null) {
			throw new NullPointerException("Key cannot be null");
		}
		if (value == null) {
			throw new NullPointerException("value cannot be null");
		}
		map.put(key, value);
	}

	@Override
	public boolean isObject() {
		return true;
	}

	@Override
	public Map<String, JSONValue> asObject() {
		return Collections.unmodifiableMap(map);
	}

	@Override
	public String toString() {
		return map.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
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
		JSONObject other = (JSONObject) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}

	@Override
	public void write(Writer writer) throws IOException {
		write(writer, 1);
	}

	private void writeNewLine(Writer writer, int tabLevel) throws IOException {
		writer.write('\n');
		for (int i = 0; i < tabLevel; i++) {
			writer.write('\t');
		}
	}

	private void write(Writer writer, int tabLevel) throws IOException {
		writer.write('{');
		writeNewLine(writer, tabLevel);

		Iterator<Map.Entry<String, JSONValue>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, JSONValue> current = it.next();
			new JSONString(current.getKey()).write(writer);
			writer.write(':');
			writer.write(' ');

			JSONValue value = current.getValue();
			if (value.isObject()) {
				((JSONObject) value).write(writer, tabLevel + 1);
			} else {
				value.write(writer);
			}

			if (it.hasNext()) {
				writer.write(", ");
				writeNewLine(writer, tabLevel);
			}
		}
		writeNewLine(writer, tabLevel - 1);
		writer.write('}');
	}
}
