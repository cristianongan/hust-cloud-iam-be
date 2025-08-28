package org.mbg.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.NoArgsConstructor;

/**
 * 
 * 31/03/2022 - LinhLH: Create new
 *
 * @author LinhLH
 */
@NoArgsConstructor
public class LocalDateUtil extends DateUtil {
	/**
	 * 
	 * @param localDate the local date
	 * @return the date string in format "dd/MM/yyyy"
	 */
	public static String formatStringShortLocalDate(LocalDate localDate) {
		return getLocalDate(localDate, SHORT_DATE_PATTERN);
	}

	/**
	 * 
	 * @param localDateTime the local date time
	 * @param locale the locale
	 * @return the formatted date string in format "HH:mm dd/MM/yyyy"
	 */
	public static String formatStringTimeAndDate(LocalDateTime localDateTime, Locale locale) {
		if (localDateTime == null) {
			return null;
		}

		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(TIME_AND_DATE_PATTERN, locale);

		return localDateTime.format(formatters);
	}

	/**
	 * 
	 * @return the time end of the date
	 */
	public static LocalDateTime getEndOfDay() {
		LocalDate localDate = LocalDate.now();

		return getEndOfDay(localDate);
	}

	/**
	 * 
	 * @param localDate the local date
	 * @return the time end of the date
	 */
	public static LocalDateTime getEndOfDay(LocalDate localDate) {
		return LocalTime.MAX.atDate(localDate);
	}

	/**
	 * 
	 * @return the first day of the month
	 */
	public static LocalDate getFirstDayOfMonth() {
		LocalDate now = LocalDate.now();

		return now.withDayOfMonth(1);
	}

	/**
	 * 
	 * @param date a date
	 * @return the first day of month of the input date
	 */
	public static LocalDate getFirstDayOfMonth(LocalDate date) {
		return date.withDayOfMonth(1);
	}
	
	/**
	 * 
	 * @return the last day of month
	 */
	public static LocalDate getLastDayOfMonth() {
		LocalDate now = LocalDate.now();

		return now.withDayOfMonth(now.lengthOfMonth());
	}

	/**
	 * 
	 * @param date a local date
	 * @return the last day of month of the given date
	 */
	public static LocalDate getLastDayOfMonth(LocalDate date) {
		return date.withDayOfMonth(date.lengthOfMonth());
	}
	
	/**
	 * 
	 * @param localDate a local date
	 * @param pattern the pattern of date that want to format
	 * @return the string date format by pattern
	 */
	public static String getLocalDate(LocalDate localDate, String pattern) {
		if (localDate == null) {
			return null;
		}

		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(pattern);

		return localDate.format(formatters);
	}

	/**
	 * 
	 * @return the time start of the day
	 */
	public static LocalDateTime getStartOfDay() {
		LocalDate localDate = LocalDate.now();

		return getStartOfDay(localDate);
	}

	/**
	 * 
	 * @param localDate a local date
	 * @return the start time of given date
	 */
	public static LocalDateTime getStartOfDay(LocalDate localDate) {
		return localDate.atStartOfDay();
	}

	/**
	 *
	 * @param date1 the first local date
	 * @param date2 the second local date
	 * @return true if date1 > date2, false otherwise
	 */
	public static boolean isAfter(LocalDate date1, LocalDate date2) {
		if (date1 == null || date2 == null) {
			return false;
		}

		return date1.isAfter(date2);
	}

	/**
	 * 
	 * @param date1 the first local date
	 * @param date2 the second local date
	 * @return true if date1 < date2, false otherwise
	 */
	public static boolean isBefore(LocalDate date1, LocalDate date2) {
		if (date1 == null || date2 == null) {
			return false;
		}

		return date1.isBefore(date2);
	}
	
	/**
	 * Parse LocalDateTime from String
	 * 
	 * @param date the string date to parse
	 * @param format the format to parse
	 * @return the parsed date
	 */
	public static LocalDateTime from(String date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		
		return LocalDateTime.parse(date, formatter);
	}
	
	/**
	 * Format String date to another format
	 * 
	 * @param dateStr the string date to format
	 * @param oldPattern the old format
	 * @param newPattern the new format
	 * @return the formatted string date by new pattern
	 */
	public static String changeFormat(String dateStr, String oldPattern, String newPattern) {
		DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(newPattern);

		LocalDateTime localDateTime = from(dateStr, oldPattern);

		return localDateTime != null ? newFormatter.format(localDateTime) : null;
	}
}
