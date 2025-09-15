package com.phantom.smartspend.utils

import java.time.*

import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {
    private val RFC3339: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    /** RFC3339 string -> LocalDate in system zone */
    fun rfc3339ToLocalDate(rfc3339: String): LocalDate {
        val odt = OffsetDateTime.parse(rfc3339, RFC3339)
        return odt.atZoneSameInstant(ZoneId.systemDefault()).toLocalDate()
    }

    /** LocalDate -> RFC3339 at start of day in UTC (common for APIs) */
    fun localDateToRfc3339Utc(date: LocalDate): String {
        return date.atStartOfDay(ZoneOffset.UTC).format(RFC3339)
    }

    /** LocalDate -> RFC3339 at start of day in system zone (use if backend wants local time) */
    fun localDateToRfc3339Local(date: LocalDate): String {
        return date.atStartOfDay(ZoneId.systemDefault()).format(RFC3339)
    }

    /** RFC3339 -> "Jan" / "Feb" etc. for chart labels */
    fun rfc3339ToMonthAbbrev(rfc3339: String, locale: Locale = Locale.ENGLISH): String {
        val odt = OffsetDateTime.parse(rfc3339, RFC3339)
        return odt.month.getDisplayName(TextStyle.SHORT, locale) // e.g., "Sep"
    }
}