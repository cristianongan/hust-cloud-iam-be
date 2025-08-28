package org.mbg.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lombok.NoArgsConstructor;

/**
 * 08/12/2022 - LinhLH: Create new
 *
 * @author LinhLH
 */
@NoArgsConstructor
public class InstantUtil extends DateUtil {
	/**
	 * 
	 * @param instant
	 * @param locale
	 * @return
	 */
	public static String formatStringTimeAndDate(Instant instant, ZoneId zoneId) {
		if (instant == null) {
			return null;
		}

		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(TIME_AND_DATE_PATTERN)
				.withZone(zoneId);

		return formatters.format(instant);
	}
	
	public static String formatStringDate(Instant instant, ZoneId zoneId) {
		if (instant == null) {
			return null;
		}

		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(SHORT_DATE_PATTERN)
				.withZone(zoneId);

		return formatters.format(instant);
	}
	
	public static Instant getInstantFromLocalDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
		if (localDateTime == null) {
			return null;
		}
		
		return localDateTime.atZone(zoneId).toInstant();
	}
}
