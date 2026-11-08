package com.example.trashoclock.ui.home

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** One document from the Firestore "Announcements" collection. */
data class Announcement(
    val id: String,
    val title: String,
    val message: String,
    val postedBy: String?,
    val datePosted: Date?,
    val imageUrl: String?,
)

/** What the Home screen's announcement section shows. An empty [Loaded] list means "no announcements yet". */
sealed interface AnnouncementUiState {
    data object Loading : AnnouncementUiState
    data class Loaded(val announcements: List<Announcement>) : AnnouncementUiState
    data class Error(val message: String) : AnnouncementUiState
}

/**
 * e.g. "Nov 7, 2026". Always in Philippine time, so the day matches what Firebase shows
 * even if the phone is set to another time zone (emulators default to UTC).
 */
fun formatAnnouncementDate(date: Date): String =
    SimpleDateFormat("MMM d, yyyy", Locale.US)
        .apply { timeZone = TimeZone.getTimeZone("Asia/Manila") }
        .format(date)
