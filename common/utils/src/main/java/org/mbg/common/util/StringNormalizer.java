/**
 * 
 */
package org.mbg.common.util;

import java.text.Normalizer;
import java.util.Optional;
import java.util.regex.Pattern;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author LinhLH
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringNormalizer {
	public static final char DEFAULT_REPLACE_CHAR = CharPool.DASH;

	public static final String DEFAULT_REPLACE = String.valueOf(DEFAULT_REPLACE_CHAR);

	private static final ImmutableMap<String, String> NON_DIACRITICS = ImmutableMap.<String, String>builder()

			// Remove crap strings with no semantics
			.put(".", "").put("\"", "").put("'", "")

			// Keep relevant characters as separation
			.put(" ", DEFAULT_REPLACE).put("]", DEFAULT_REPLACE).put("[", DEFAULT_REPLACE).put(")", DEFAULT_REPLACE)
			.put("(", DEFAULT_REPLACE).put("=", DEFAULT_REPLACE).put("!", DEFAULT_REPLACE).put("/", DEFAULT_REPLACE)
			.put("\\", DEFAULT_REPLACE).put("&", DEFAULT_REPLACE).put(",", DEFAULT_REPLACE).put("?", DEFAULT_REPLACE)
			.put("°", DEFAULT_REPLACE) // Remove ?? is diacritic?
			.put("|", DEFAULT_REPLACE).put("<", DEFAULT_REPLACE).put(">", DEFAULT_REPLACE).put(";", DEFAULT_REPLACE)
			.put(":", DEFAULT_REPLACE).put("_", DEFAULT_REPLACE).put("#", DEFAULT_REPLACE).put("~", DEFAULT_REPLACE)
			.put("+", DEFAULT_REPLACE).put("*", DEFAULT_REPLACE)

			// Replace non-diacritics as their equivalent characters
			.put("\u0141", "l") // BiaLy stock
			.put("\u0142", "l") // Bialystok
			.put("ß", "ss").put("æ", "ae").put("ø", "o").put("©", "c").put("\u00D0", "d") // All Ð ð from
																							// http://de.wikipedia.org/wiki/%C3%90
			.put("\u00F0", "d").put("\u0110", "d").put("\u0111", "d").put("\u0189", "d").put("\u0256", "d")
			.put("\u00DE", "th") // thorn Þ
			.put("\u00FE", "th") // thorn þ
			.build();

	public static String normalize(String original) {
		String str = original;

		if (str == null) {
			return null;
		}

		str = stripDiacritics(str);

		str = stripNonDiacritics(str);

		if (str.length() == 0) {
			// Ugly special case to work around non-existing empty strings
			// in Oracle. Store original scrapping as simplified.
			// It would return an empty string if Oracle could store it.
			return original;
		}

		return str.toLowerCase();
	}

	public static String stripNonDiacritics(String original) {
		StringBuilder ret = new StringBuilder();

		String lastChar = null;

		for (int i = 0; i < original.length(); i++) {
			String source = original.substring(i, i + 1);

			String replace = NON_DIACRITICS.get(source);

			String toReplace = Optional.ofNullable(replace).orElse(source);

			if (DEFAULT_REPLACE.equals(lastChar) && DEFAULT_REPLACE.equals(toReplace)) {
				toReplace = StringPool.BLANK;
			} else {
				lastChar = toReplace;
			}

			ret.append(toReplace);
		}

		if (ret.length() > 0 && DEFAULT_REPLACE_CHAR == ret.charAt(ret.length() - 1)) {
			ret.deleteCharAt(ret.length() - 1);
		}

		return ret.toString();
	}

	/*
	 * Special regular expression character ranges relevant for simplification -> see
	 * http://docstore.mik.ua/orelly/perl/prog3/ch05_04.htm InCombiningDiacriticalMarks: special marks
	 * that are part of "normal" ä, ö, î etc.. IsSk: Symbol, Modifier see
	 * http://www.fileformat.info/info/unicode/category/Sk/list.htm IsLm: Letter, Modifier see
	 * http://www.fileformat.info/info/unicode/category/Lm/list.htm
	 */
	public static final Pattern DIACRITICS_AND_FRIENDS =
			Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

	public static String stripDiacritics(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);

		str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");

		return str;
	}
}
