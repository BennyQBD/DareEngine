package engine.util.parsing.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;

import engine.util.parsing.TokenReader;

public class JSONNumber extends JSONValue {
	public static JSONValue parse(TokenReader tokens, String currentToken)
			throws IOException, ParseException {
		return new JSONNumber(currentToken);
	}
	
	private String value;
	
	public JSONNumber(String value) {
		if(value == null) {
			throw new NullPointerException("Number cannot have a null value");
		}
		this.value = value;
	}
	
	@Override
	public boolean isNumber() {
		return true;
	}
	
	@Override
	public int asInt() {
		return Integer.parseInt(value);
	}
	
	@Override
	public long asLong() {
		return Long.parseLong(value);
	}
	
	@Override
	public float asFloat() {
		return Float.parseFloat(value);
	}
	
	@Override
	public double asDouble() {
		return Double.parseDouble(value);
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
		JSONNumber other = (JSONNumber) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(value);
	}
}
