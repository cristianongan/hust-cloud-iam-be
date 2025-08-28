package org.mbg.common.util;

import java.text.Format;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.time.FastDateFormat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 *
 * @author os_linhlh2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FastDateFormatFactoryUtil {

	private static final Map<String, Format> dateFormats = new ConcurrentHashMap<>();
	
	private static final Map<String, Format> dateTimeFormats = new ConcurrentHashMap<>();
	
	private static final Locale locale = Locale.of("vi", "VN");
	
	private static final Map<String, Format> simpleDateFormats = new ConcurrentHashMap<>();

	private static final Map<String, Format> timeFormats = new ConcurrentHashMap<>();

	public static final int FULL = 0;

	public static final int LONG = 1;

	public static final int MEDIUM = 2;

	public static final int SHORT = 3;

	public static Format getDate(int style, Locale locale, TimeZone timeZone) {
		String key = getKey(style, locale, timeZone);

		Format format = dateFormats.get(key);

		if (format == null) {
			format = FastDateFormat.getDateInstance(style, timeZone, locale);

			dateFormats.put(key, format);
		}

		return format;
	}

	public static Format getDate(Locale locale) {
		return getDate(locale, null);
	}

	public static Format getDate(Locale locale, TimeZone timeZone) {
		return getDate(SHORT, locale, timeZone);
	}

	public static Format getDate(TimeZone timeZone) {
		return getDate(locale, timeZone);
	}

	public static Format getDateTime(int dateStyle, int timeStyle, Locale locale, TimeZone timeZone) {

		String key = getKey(dateStyle, timeStyle, locale, timeZone);

		Format format = dateTimeFormats.get(key);

		if (format == null) {
			format = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);

			dateTimeFormats.put(key, format);
		}

		return format;
	}

	public static Format getDateTime(Locale locale) {
		return getDateTime(locale, null);
	}

	public static Format getDateTime(Locale locale, TimeZone timeZone) {
		return getDateTime(SHORT, SHORT, locale, timeZone);
	}

	public static Format getDateTime(TimeZone timeZone) {
		return getDateTime(locale, timeZone);
	}

	protected static String getKey(Object... arguments) {
		StringBuilder sb = new StringBuilder(arguments.length * 2 - 1);

		for (int i = 0; i < arguments.length; i++) {
			sb.append(arguments[i]);

			if ((i + 1) < arguments.length) {
				sb.append(StringPool.UNDERLINE);
			}
		}

		return sb.toString();
	}

	public static Format getSimpleDateFormat(String pattern) {
		return getSimpleDateFormat(pattern, locale, null);
	}

	public static Format getSimpleDateFormat(String pattern, Locale locale) {
		return getSimpleDateFormat(pattern, locale, null);
	}

	public static Format getSimpleDateFormat(String pattern, Locale locale, TimeZone timeZone) {

		String key = getKey(pattern, locale, timeZone);

		Format format = simpleDateFormats.get(key);

		if (format == null) {
			format = FastDateFormat.getInstance(pattern, timeZone, locale);

			simpleDateFormats.put(key, format);
		}

		return format;
	}

	public static Format getSimpleDateFormat(String pattern, TimeZone timeZone) {
		return getSimpleDateFormat(pattern, locale, timeZone);
	}

	public static Format getTime(int style, Locale locale, TimeZone timeZone) {
		String key = getKey(style, locale, timeZone);

		Format format = timeFormats.get(key);

		if (format == null) {
			format = FastDateFormat.getTimeInstance(style, timeZone, locale);

			timeFormats.put(key, format);
		}

		return format;
	}

	public static Format getTime(Locale locale) {
		return getTime(locale, null);
	}

	public static Format getTime(Locale locale, TimeZone timeZone) {
		return getTime(SHORT, locale, timeZone);
	}

	public static Format getTime(TimeZone timeZone) {
		return getTime(locale, timeZone);
	}
}
