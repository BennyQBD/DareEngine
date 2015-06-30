package engine.parsing.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import engine.parsing.TokenReader;

public abstract class JSONValue {
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
	
	public static JSONValue create(int value) {
		return new JSONNumber(value);
	}
	
	public static JSONValue create(long value) {
		return new JSONNumber(value);
	}
	
	public static JSONValue create(float value) {
		return new JSONNumber(value);
	}
	
	public static JSONValue create(double value) {
		return new JSONNumber(value);
	}
	
	public static JSONValue create(short value) {
		return new JSONNumber(value);
	}
	
	public static JSONValue create(byte value) {
		return new JSONNumber(value);
	}
	
	public static JSONValue create(char value) {
		return new JSONString(value);
	}
	
	public static JSONValue create(boolean value) {
		return JSONLiteral.create(value);
	}
	
	public static JSONValue create(String value) {
		if(value == null) {
			return JSONLiteral.NULL;
		} else {
			return new JSONString(value);
		}
	}

	public abstract void write(Writer writer) throws IOException;

	public boolean isObject() {
		return false;
	}

	public boolean isArray() {
		return false;
	}

	public boolean isString() {
		return false;
	}

	public boolean isNumber() {
		return false;
	}

	public boolean isBoolean() {
		return false;
	}

	public boolean isNull() {
		return false;
	}

	public Map<String, JSONValue> asObject() {
		throw new UnsupportedOperationException("Not an object: " + toString());
	}

	public List<JSONValue> asArray() {
		throw new UnsupportedOperationException("Not an array: " + toString());
	}

	public String asString() {
		throw new UnsupportedOperationException("Not a string: " + toString());
	}

	public int asInt() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	public long asLong() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	public float asFloat() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	public double asDouble() {
		throw new UnsupportedOperationException("Not a number: " + toString());
	}

	public boolean asBoolean() {
		throw new UnsupportedOperationException("Not a boolean: " + toString());
	}
}
