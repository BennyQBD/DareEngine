/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

/**
 * Reads and tokenizes a source.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class TokenReader implements AutoCloseable {
	private Reader reader;
	private boolean isBackedUp;
	private int backedUpChar;
	private int lineNumber;

	/**
	 * Creates a new TokenReader.
	 * 
	 * @param reader
	 *            The source of tokens.
	 */
	public TokenReader(Reader reader) {
		this.reader = new BufferedReader(reader);
		this.isBackedUp = false;
		this.backedUpChar = -1;
		this.lineNumber = 0;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	/**
	 * Gets the next token from this reader. A token can be a string that starts
	 * with an alphabetical character, a number in integer, decimal, or
	 * exponential format, a quoted piece of text, or a single character symbol.
	 * <p/>
	 * For example, this string: fooBar 13\234.18.9 123e37lol';123'sd;sa <br/>
	 * would produce these tokens: fooBar <br/>
	 * 13 <br/>
	 * / <br/>
	 * 234.18 <br/>
	 * .9 <br/>
	 * 123e37 <br/>
	 * lol <br/>
	 * ' <br/>
	 * ; <br/>
	 * 123 <br/>
	 * ' <br/>
	 * sd <br/>
	 * ; <br/>
	 * sa <br/>
	 * 
	 * @return The next token from this reader, or null if there are no more
	 *         tokens.
	 * @throws IOException
	 *             If the token fails to read.
	 */
	public String next() throws IOException {
		int next = skipWhite(getChar());
		if (next == -1) {
			return null;
		} else if (isAlpha(next)) {
			return getName(next);
		} else if (isNumber(next)) {
			return getNumber(next);
		} else if (isQuote(next)) {
			return getString(next);
		} else {
			return getSymbol(next);
		}
	}

	/**
	 * Asserts that some condition is true. If it is not, throws a
	 * ParseException with a given message.
	 * 
	 * @param condition
	 *            The condition that must be true.
	 * @param message
	 *            The error message if the condition is not true.
	 * @throws ParseException
	 *             If the condition is not true.
	 */
	public void parseAssert(boolean condition, String message)
			throws ParseException {
		if (!condition) {
			throw new ParseException(message + " (line " + lineNumber + ")",
					lineNumber);
		}
	}

	private String getSymbol(int next) {
		return ((char) next) + "";
	}

	private String getNumber(int next) throws IOException {
		StringBuilder str = new StringBuilder();
		boolean hasDecimalPlace = false;

		while (isDigit(next) || (!hasDecimalPlace && next == '.')) {
			str.append((char) next);
			if (next == '.') {
				hasDecimalPlace = true;
			}
			next = getChar();
		}
		next = readExponent(next, str);
		ungetChar(next);
		return str.toString();
	}

	private int readExponent(int next, StringBuilder str) throws IOException {
		if (!isExponent(next)) {
			return next;
		}

		str.append((char) next);
		next = getChar();

		if (isNumber(next)) {
			str.append((char) next);
			next = getChar();
		}

		while (isDigit(next = getChar())) {
			str.append((char) next);
		}

		return next;
	}

	private String getName(int next) throws IOException {
		StringBuilder str = new StringBuilder();
		while (isAlNum(next)) {
			str.append((char) next);
			next = getChar();
		}
		ungetChar(next);
		return str.toString();
	}

	private String getString(int next) throws IOException {
		StringBuilder str = new StringBuilder();
		str.append((char) next);
		while (!isQuote((next = getChar()))) {
			// TODO: Properly read escape sequences
			str.append((char) next);
		}
		str.append((char) next);
		return str.toString();
	}

	private void ungetChar(int next) {
		assert isBackedUp == false;
		isBackedUp = true;
		backedUpChar = next;
	}

	private int getChar() throws IOException {
		if (isBackedUp) {
			isBackedUp = false;
			return backedUpChar;
		}
		int result = reader.read();
		if (isNewLine(result)) {
			lineNumber++;
		}
		return result;
	}

	private int skipWhite(int c) throws IOException {
		while (isWhitespace(c)) {
			c = getChar();
		}
		return c;
	}

	/**
	 * Determines if a character represents the start of exponential notation.
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isExponent(int c) {
		return c == 'e' || c == 'E';
	}

	/**
	 * Determines if a character is a new line character.
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isNewLine(int c) {
		return c == '\n';
	}

	/**
	 * Determines if a character is whitespace.
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isWhitespace(int c) {
		return isNewLine(c) || c == '\r' || c == ' ' || c == '\t';
	}

	/**
	 * Determines if a character could be part of a number
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isNumber(int c) {
		return isDigit(c) || c == '-' || c == '+' || c == '.';
	}

	/**
	 * Determines if a character is a digit
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isDigit(int c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * Determines if a character is either alphabetic or numeric.
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isAlNum(int c) {
		return isAlpha(c) || isDigit(c);
	}

	/**
	 * Determines if a character is a quote.
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isQuote(int c) {
		return c == '\"';
	}

	/**
	 * Determines if a character is alphabetical.
	 * 
	 * @param c The character of interest.
	 * @return True if it is, false otherwise.
	 */
	public static boolean isAlpha(int c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}
}
