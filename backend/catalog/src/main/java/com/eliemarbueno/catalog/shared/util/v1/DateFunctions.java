package com.eliemarbueno.catalog.shared.util.v1;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateFunctions {

	public static OffsetDateTime getNow() {
		return OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).withNano(0);
		// OffsetDateTime.now().withNano(0); same of this, but to confirm if server
		// ignore parameter spring.jackson.time-zone
	}

	public static OffsetDateTime getNow(ZoneId zoneId) {
		ZonedDateTime zdt = ZonedDateTime.now(zoneId);
		ZoneOffset offset = zdt.getOffset();

		return OffsetDateTime.ofInstant(zdt.toInstant(), offset).withNano(0);
	}

	public static OffsetDateTime getNowAlternative(ZoneId zoneId) {
		ZonedDateTime zdt = ZonedDateTime.now(zoneId);
		return zdt.toOffsetDateTime().withNano(0);
	}

	public static long calculateDifference(OffsetDateTime dateTime) {
		return Duration.between(OffsetDateTime.now(), dateTime).toMinutes();
	}

	public static OffsetDateTime addMinutesToDate(OffsetDateTime date, Long minutes) {
		return date.plusMinutes(minutes);
	}

	public static OffsetDateTime addSecondsToDate(OffsetDateTime date, Long seconds) {
		return date.plusSeconds(seconds);
	}

	public static String getDateTimeWithTimezone(OffsetDateTime offsetDateTime, String timezone, String dateFormat) {
		return offsetDateTime.atZoneSameInstant(ZoneId.of(timezone)).format(DateTimeFormatter.ofPattern(dateFormat));
	}

}
