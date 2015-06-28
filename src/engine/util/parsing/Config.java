package engine.util.parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Config {
	private Map<String, String> map;

	public static void write(String fileName, Map<String, String> map)
			throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> pair = it.next();
				String line = pair.getKey() + "=" + pair.getValue() + "\n";
				bw.write(line);
			}
		}
	}

	public Config(String fileName) throws IOException, ParseException {
		map = new HashMap<String, String>();

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				if (line.isEmpty()) {
					continue;
				}

				char start = line.charAt(0);
				if (start == '[' || start == '#') {
					continue;
				}
				String[] tokens = line.split("=");
				if (tokens.length != 2) {
					throw new ParseException("Line has too many '='",
							lineNumber);
				}

				map.put(tokens[0].trim(), tokens[1].trim());
			}
		}
	}

	public String getString(String entry) {
		String result = map.get(entry);
		if(result != null && result.charAt(0) == '$') {
			return getString(result.substring(1));
		}
		return result;
	}
	
	public int getInt(String entry) {
		return Integer.parseInt(getString(entry));
	}

	public double getDouble(String entry) {
		return Double.parseDouble(getString(entry));
	}
	
	public boolean getBoolean(String entry) {
		return Boolean.parseBoolean(getString(entry));
	}
	
	public String getStringWithDefault(String entry, String defaultEntry) {
		String result = getString(entry);
		if(result == null) {
			result = getString(defaultEntry);
		}
		return result;
	}
	
	public int getIntWithDefault(String entry, String defaultEntry) {
		return Integer.parseInt(getStringWithDefault(entry, defaultEntry));
	}
	
	public double getDoubleWithDefault(String entry, String defaultEntry) {
		return Double.parseDouble(getStringWithDefault(entry, defaultEntry));
	}
	
	public boolean getBooleanWithDefault(String entry, String defaultEntry) {
		return Boolean.parseBoolean(getStringWithDefault(entry, defaultEntry));
	}
}
