package engine.util.parsing.json;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;

import engine.util.parsing.TokenReader;

public class JSONLiteral extends JSONValue {
	public static JSONValue parse(TokenReader tokens, String currentToken) throws IOException, ParseException {
		switch(currentToken.charAt(0)) {
		case 'n':
			return parseNull(tokens, currentToken);
		case 't':
			return parseTrue(tokens, currentToken);
		case 'f':
			return parseFalse(tokens, currentToken);
		}
		tokens.parseAssert(false, currentToken + " is not a JSONLiteral");
		return null;
	}
	
	private static JSONValue parseFalse(TokenReader tokens, String token) throws ParseException {
		tokens.parseAssert(token.equals("false"), "'false' expected");
		return FALSE;
	}

	private static JSONValue parseTrue(TokenReader tokens, String token) throws ParseException {
		tokens.parseAssert(token.equals("true"), "'true' expected");
		return TRUE;
	}

	private static JSONValue parseNull(TokenReader tokens, String token) throws ParseException {
		tokens.parseAssert(token.equals("null"), "'null' expected");
		return NULL;
	}
	
	public static final JSONLiteral NULL = new JSONLiteral("null");
	public static final JSONLiteral TRUE = new JSONLiteral("true");
	public static final JSONLiteral FALSE = new JSONLiteral("false");
	
	private String string;
	
	private JSONLiteral(String string) {
		this.string = string;
	}
	
	@Override
	public boolean isBoolean() {
		return this == TRUE || this == FALSE;
	}
	
	@Override
	public boolean isNull() {
		return this == NULL;
	}
	
	@Override
	public boolean asBoolean() {
		if(this == TRUE) {
			return true;
		} else if(this == FALSE) {
			return false;
		} else {
			return super.asBoolean();
		}
	}
	
	@Override
	public String toString() {
		return string;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(string);
	}
}
