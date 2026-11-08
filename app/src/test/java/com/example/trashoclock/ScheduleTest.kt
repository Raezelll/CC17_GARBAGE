package com.example.trashoclock

import com.example.trashoclock.ui.home.fullDate
import com.example.trashoclock.ui.home.shortDayName
import com.example.trashoclock.ui.home.weekOf
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ScheduleTest {
    private fun labels(today: LocalDate) = weekOf(today).map { "${shortDayName(it)} ${it.dayOfMonth}" }

    @Test
    fun wednesdayShowsItsSundayToSaturdayWeek() {
        assertEquals(
            listOf("Sun 11", "Mon 12", "Tue 13", "Wed 14", "Thu 15", "Fri 16", "Sat 17"),
            labels(LocalDate.of(2026, 10, 14)),
        )
    }

    @Test
    fun sundayStartsItsOwnWeek() {
        assertEquals(LocalDate.of(2026, 10, 11), weekOf(LocalDate.of(2026, 10, 11)).first())
    }

    @Test
    fun saturdayEndsTheWeek() {
        assertEquals(LocalDate.of(2026, 10, 17), weekOf(LocalDate.of(2026, 10, 17)).last())
    }

    @Test
    fun weekCanSpanMonthAndYear() {
        assertEquals(
            listOf("Sun 27", "Mon 28", "Tue 29", "Wed 30", "Thu 31", "Fri 1", "Sat 2"),
            labels(LocalDate.of(2026, 12, 31)),
        )
    }

    @Test
    fun fullDateFormat() {
        assertEquals("Oct 14, 2026", fullDate(LocalDate.of(2026, 10, 14)))
    }
}
