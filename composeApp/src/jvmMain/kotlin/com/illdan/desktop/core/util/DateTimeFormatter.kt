package com.illdan.desktop.core.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object DateTimeFormatter {

    data class YMD(
        val year: Int,
        val month: Int,
        val day: Int
    )

    fun parseYMD(date: String): YMD {
        val (year, month, day) = date.split("-").map{ it.toInt() }
        return YMD(year, month, day)
    }

    fun diffBetweenDate(date1: String, date2: String): Int {
        val (year1, month1, day1) = parseYMD(date1)
        val (year2, month2, day2) = parseYMD(date2)

        val date1 = LocalDate(year1, month1, day1)
        val date2 = LocalDate(year2, month2, day2)

        // date2 - date1 반환
        return date1.daysUntil(date2)
    }

    @OptIn(ExperimentalTime::class)
    fun formatDeadline(deadline: String): String {
        if (deadline.isBlank()) return ""

        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val todayStr = "${today.year}-${today.month.number.toString().padStart(2, '0')}-${today.day.toString().padStart(2, '0')}"

        val diff = diffBetweenDate(todayStr, deadline)

        return when {
            diff > 0 -> "D-$diff"
            diff < 0 -> "D+$diff"
            else -> "D-Day"
        }
    }
}