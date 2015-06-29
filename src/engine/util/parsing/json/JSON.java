package engine.util.parsing.json;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import engine.util.parsing.TokenReader;

public class JSON {
	private JSONValue value;
	
	public JSON(String fileName) throws IOException, ParseException {
		TokenReader tokens;
		tokens = new TokenReader(new FileReader(fileName));
		value = JSONValue.parse(tokens, tokens.next());

		String token;
		tokens.parseAssert((token = tokens.next()) == null,
				"Expected EOF; instead got " + token);
		
		tokens.close();
	}
	
	public JSON(JSONValue value) {
		this.value = value;
	}
	
	public void write(String fileName) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
		value.write(br);
		br.close();
	}
	
	public JSONValue get() {
		return value;
	}
}
