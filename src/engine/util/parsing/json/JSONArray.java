package engine.util.parsing.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import engine.util.parsing.TokenReader;

public class JSONArray extends JSONValue {
	public static JSONValue parse(TokenReader tokens, String token)
			throws IOException, ParseException {
		JSONArray result = new JSONArray();

		if ((token = tokens.next()).equals("]")) {
			return result;
		}

		do {
			result.add(JSONValue.parse(tokens, token));
			if(!(token = tokens.next()).equals(",")) {
				break;
			}
			token = tokens.next();
		} while(true);

		tokens.parseAssert(token.equals("]"), "Closing ']' expected!");
		return result;
	}
	
	private List<JSONValue> values;
	
	public JSONArray() {
		values = new ArrayList<JSONValue>();
	}
	
	public void add(JSONValue value) {
		if(value == null) {
			throw new NullPointerException("JSONValue cannot be null");
		}
		values.add(value);
	}
	
	@Override
	public boolean isArray() {
		return true;
	}
	
	@Override
	public List<JSONValue> asArray() {
		return Collections.unmodifiableList(values);
	}
	
	@Override
	public String toString() {
		return values.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		JSONArray other = (JSONArray) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write('[');
		Iterator<JSONValue> it = values.iterator();
		while(it.hasNext()) {
			it.next().write(writer);
			if(it.hasNext()) {
				writer.write(", ");
			}
		}
		writer.write(']');
	}
}
