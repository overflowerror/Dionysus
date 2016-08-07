package net.persei.dionysus.blocks;

import java.util.Map;

import net.persei.dionysus.Data;
import net.persei.dionysus.Setup;
import net.persei.dionysus.exceptions.DataValueNotAvailableException;
import net.persei.dionysus.exceptions.MalformedBlockConditionException;

public class BlockCondition {
	
	private String condition;
	
	public BlockCondition(String condition) {
		condition = condition.replace(" ", "");
		condition = condition.replace("	", "");
		condition = condition.replace("\n", "");
		condition = condition.replace("\r", "");
		
		this.condition = condition;
	}
	
	public boolean parse(Data input) throws MalformedBlockConditionException, DataValueNotAvailableException {
		String tmp = new String(condition);
		
		while(true) {
			if (tmp.indexOf("(") < 0)
				break;
			
			int open = 0;
			while(tmp.indexOf("(", open + 1) >= 0) {
				open = tmp.indexOf("(", open + 1);
			}
			int close = tmp.indexOf(")", open);
			
			if (close == -1)
				throw new MalformedBlockConditionException();
			
			String sub = tmp.substring(open + 1, close - 1);
			String before = tmp.substring(0, open - 1);
			String after = tmp.substring(close + 1);
			tmp = before + (minTerm(sub, input)) + after;
		}
		
		return minTerm(tmp, input);
	}
	private boolean minTerm(String sub, Data input) throws DataValueNotAvailableException, MalformedBlockConditionException {
		if (sub.equals("true"))
			return true;
		if (sub.equals("false"))
			return false;
		if (sub.indexOf("&") >= 0)
			return minTerm(sub.substring(0, sub.indexOf("&")), input) &&
					minTerm(sub.substring(sub.indexOf("&") + 1), input);
		if (sub.indexOf("|") >= 0)
			return minTerm(sub.substring(0, sub.indexOf("|")), input) ||
					minTerm(sub.substring(sub.indexOf("|") + 1), input);
		if (sub.indexOf("^") >= 0)
			return minTerm(sub.substring(0, sub.indexOf("^")), input) ^
					minTerm(sub.substring(sub.indexOf("^") + 1), input);
		if (sub.indexOf("!") >= 0)
			return ! minTerm(sub.substring(sub.indexOf("!") + 1), input);
		if (sub.indexOf("==") >= 0)
			return stringValue(sub.substring(0, sub.indexOf("==")), input).equals(stringValue(sub.substring(sub.indexOf("==") + 1), input));
		if (sub.indexOf(">") >= 0)
			return intValue(sub.substring(0, sub.indexOf(">")), input) > intValue(sub.substring(sub.indexOf(">") + 1), input);
		if (sub.indexOf("<") >= 0)
			return intValue(sub.substring(0, sub.indexOf("<")), input) < intValue(sub.substring(sub.indexOf("<") + 1), input);
		if (sub.indexOf(">=") >= 0)
			return intValue(sub.substring(0, sub.indexOf(">=")), input) >= intValue(sub.substring(sub.indexOf(">=") + 1), input);
		if (sub.indexOf("<=") >= 0)
			return intValue(sub.substring(0, sub.indexOf("<=")), input) >= intValue(sub.substring(sub.indexOf("<=") + 1), input);
		if (sub.indexOf("data") >= 0)
			return minTerm(getDataValue(sub, input), input);
		
		throw new MalformedBlockConditionException();
	}

	private int intValue(String substring, Data input) {
		try {
			return Integer.parseInt(substring);
		} catch (NumberFormatException e) {
		}
		try {
			return Integer.parseInt(getDataValue(substring, input));
		} catch (NumberFormatException e) {
		} catch (Exception e) {
		}
		throw new NumberFormatException();
	}

	private String getDataValue(String string, Data input) throws DataValueNotAvailableException {
		if (string.equals("setup.state"))
			return ((Setup) input.getEntry("setup")).getState();
		if (string.indexOf("data.") == 0) {
			String key = string.substring("data.".length() + 1);
			if (input.getEntry("blockData") != null) {
				Map<String, String> blockData = (Map<String, String>) input.getEntry("blockData");
				if (blockData.get(key) != null)
					return blockData.get(key);
			}
		}
		throw new DataValueNotAvailableException();
	}

	private String stringValue(String string, Data input) throws DataValueNotAvailableException {
		if (string.substring(0, 1).equals("\"") && string.substring(string.length() - 1).equals("\""))
			return string.substring(1, string.length() - 1);
		
		return getDataValue(string, input);
	}
}
