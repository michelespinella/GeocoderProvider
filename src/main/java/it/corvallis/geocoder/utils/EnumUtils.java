package it.corvallis.geocoder.utils;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * @author Roberto Mozzicato
 */
public class EnumUtils {

   public static <Q extends Enum<?>> Q readEnum(String valore, Q[] enumValues) {
        for (Q tempVal : enumValues) {
            if (tempVal.toString() == null && valore == null || tempVal.name().equals(valore) || (tempVal instanceof ValueEnum && ((ValueEnum)tempVal).getValue().equals(valore)) || tempVal.toString().equals(valore)) {
                return tempVal;
            }
        }

        return null;
    }

	public static String[] enumToStringArray(Enum<?>[] enumArray, boolean decorate, boolean htmlStrings) {
		String[] ret = null;
		if (enumArray != null) {
			ret = new String[enumArray.length];
			for (int i = 0; i < enumArray.length; i++) {
				if (enumArray[i] instanceof ValueEnum)
					ret[i] = ((ValueEnum) enumArray[i]).getValue();
				else {
					String tmpStr = enumArray[i].toString();
					if (decorate) {
						StringBuffer sb = new StringBuffer();
						// Sostituisce gli underscore con spazi e capitalizza le parole
						int pos = 0, pos2 = tmpStr.indexOf('_', pos + 1);

						while (pos >= 0 && pos < tmpStr.length()) {
							pos2 = tmpStr.indexOf('_', pos + 1);
							if (pos2 < 0)
								pos2 = tmpStr.length();
							sb.append(Character.toUpperCase(tmpStr.charAt(pos)));
							sb.append(tmpStr.substring(pos + 1, pos2).toLowerCase());
							sb.append(' ');
							pos = pos2 + 1;
						}
						ret[i] = sb.substring(0, sb.length() - 1);
					}
					else
						ret[i] = tmpStr;
				}

				if (htmlStrings) {
					ret[i] = stringToHTMLString(ret[i]);
				}
			}
		}

		return ret;
	}

	public static String[] enumToStringArray(Enum<?>[] enumArray) {
		return enumToStringArray(enumArray, false, false);
	}

	public static LinkedHashMap<Serializable, String> enumToValueMap(Enum<?>[] enumArray) {
		LinkedHashMap<Serializable, String> ret = new LinkedHashMap<Serializable, String>();
		String[] labels = enumToStringArray(enumArray, true, true);

		for (int i = 0; i < enumArray.length; i++)
			if (enumArray instanceof KeyValueEnum[]) {
				ret.put(((KeyValueEnum)enumArray[i]).getKey(),  labels[i]);	
			}
			else {
				ret.put(enumArray[i].name(), labels[i]);	
			}

		return ret;
	}

	public static String stringToHTMLString(String string) {
		StringBuffer sb = new StringBuffer(string.length());
		// true if last char was blank
		boolean lastWasBlankChar = false;
		int len = string.length();
		char c;

		for (int i = 0; i < len; i++) {
			c = string.charAt(i);
			if (c == ' ') {
				// blank gets extra work,
				// this solves the problem you get if you replace all
				// blanks with &nbsp;, if you do that you loss 
				// word breaking
				if (lastWasBlankChar) {
					lastWasBlankChar = false;
					sb.append("&nbsp;");
				}
				else {
					lastWasBlankChar = true;
					sb.append(' ');
				}
			}
			else {
				lastWasBlankChar = false;
				//
				// HTML Special Chars
				if (c == '"')
					sb.append("&quot;");
				else if (c == '&')
					sb.append("&amp;");
				else if (c == '<')
					sb.append("&lt;");
				else if (c == '>')
					sb.append("&gt;");
				else if (c == '\n')
					// Handle Newline
					sb.append("&lt;br/&gt;");
				else {
					int ci = 0xffff & c;
					if (ci < 160)
						// nothing special only 7 Bit
						sb.append(c);
					else {
						// Not 7 Bit use the unicode system
						sb.append("&#");
						sb.append(Integer.valueOf(ci).toString());
						sb.append(';');
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static boolean isEqual(Object elementToFind, Enum<?>[] elementsToCompare) {
		return isEqual(elementToFind, (Object[])elementsToCompare);
	}

	public static boolean isEqual(Object elementToFind, Object... elementsToCompare) {
		if (elementsToCompare.length==0)
			return false;

		if (elementToFind == null) {
			return false;
		}

		for(int i=0;i<elementsToCompare.length;i++) {
			if (elementsToCompare[i] == null) {
				return false;
			} else {
				if (elementToFind.toString().equals(elementsToCompare[i].toString())) {
					return true;
				}
			}
		}

		return false;
	}
	
}
