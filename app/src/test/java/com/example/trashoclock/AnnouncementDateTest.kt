package com.example.trashoclock

import com.example.trashoclock.ui.home.formatAnnouncementDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.util.Date
import java.util.TimeZone

class AnnouncementDateTest {
    @Test
    fun midnightInManilaStaysOnTheSameDayOnAUtcPhone() {
        val original = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        try {
            // Nov 7, 2026 12:00 AM UTC+8 is still Nov 6 in UTC
            val midnightManila = Date.from(Instant.parse("2026-11-06T16:00:00Z"))
            assertEquals("Nov 7, 2026", formatAnnouncementDate(midnightManila))
        } finally {
            TimeZone.setDefault(original)
        }
    }
}
