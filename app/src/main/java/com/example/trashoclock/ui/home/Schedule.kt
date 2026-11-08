package com.example.trashoclock.ui.home

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/** The Sunday-to-Saturday week containing [date]. */
fun weekOf(date: LocalDate): List<LocalDate> {
    // DayOfWeek numbers run Monday = 1 … Sunday = 7, so "% 7" gives days since Sunday
    val sunday = date.minusDays((date.dayOfWeek.value % 7).toLong())
    return (0L..6L).map { sunday.plusDays(it) }
}

/** "Sun", "Mon", … */
fun shortDayName(date: LocalDate): String = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)

private val FullDateFormat = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US)

/** "Oct 14, 2026" */
fun fullDate(date: LocalDate): String = date.format(FullDateFormat)

/** The big title above the week: "Today" for today, otherwise the weekday, e.g. "Friday". */
fun dayTitle(date: LocalDate, today: LocalDate): String =
    if (date == today) "Today" else date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)

/** The hour rows shown in the schedule card. */
val ScheduleHours = listOf("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM")

/** A pick-up at row [hourIndex] of [ScheduleHours]. */
data class PickUp(val hourIndex: Int, val place: String)

/**
 * Sample pick-ups by weekday, so every week has the same pattern. Monday matches the
 * Figma design; Sunday, Tuesday and Friday have none.
 */
private val SamplePickUps: Map<DayOfWeek, List<PickUp>> = mapOf(
    DayOfWeek.MONDAY to listOf(PickUp(0, "45 Rd. Cornelia St."), PickUp(5, "45 Rd. Ronan St.")),
    DayOfWeek.WEDNESDAY to listOf(PickUp(1, "12 Mabini St."), PickUp(4, "7 Rizal Ave.")),
    DayOfWeek.THURSDAY to listOf(PickUp(2, "45 Rd. Cornelia St.")),
    DayOfWeek.SATURDAY to listOf(PickUp(0, "3 Luna St."), PickUp(3, "45 Rd. Ronan St.")),
)

fun pickUpsOn(date: LocalDate): List<PickUp> = SamplePickUps[date.dayOfWeek].orEmpty()
