package com.example.trashoclock

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trashoclock.ui.home.Announcement
import com.example.trashoclock.ui.home.AnnouncementUiState
import com.example.trashoclock.ui.home.HomeScreen
import com.example.trashoclock.ui.theme.TrashOClockTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.GregorianCalendar
import java.util.TimeZone

/** Feeds Home fixed announcement states, since live Firestore data changes. */
@RunWith(AndroidJUnit4::class)
class AnnouncementTest {
    @get:Rule val rule = createComposeRule()

    private fun show(state: AnnouncementUiState) =
        rule.setContent { TrashOClockTheme { HomeScreen(PaddingValues(), announcements = state) } }

    @Test
    fun cardOpensAndClosesPopup() {
        val announcement = Announcement(
            id = "a1",
            title = "Collection moved to Friday",
            message = "Full details of the schedule change.",
            postedBy = "Barangay Hall",
            datePosted = GregorianCalendar(TimeZone.getTimeZone("Asia/Manila")).apply { set(2026, 10, 7, 0, 0, 0) }.time,
            imageUrl = null,
        )
        show(AnnouncementUiState.Loaded(listOf(announcement)))

        rule.onNodeWithText("Posted by Barangay Hall · Nov 7, 2026").assertIsDisplayed()
        rule.onAllNodesWithContentDescription("Close").assertCountEquals(0)
        rule.onNodeWithText("View more", substring = true).performClick()
        // The popup repeats the title, so it now appears twice
        rule.onAllNodesWithText("Collection moved to Friday").assertCountEquals(2)
        rule.onNodeWithContentDescription("Close").performClick()
        rule.onAllNodesWithContentDescription("Close").assertCountEquals(0)
    }

    @Test
    fun emptyListShowsEmptyMessage() {
        show(AnnouncementUiState.Loaded(emptyList()))
        rule.onNodeWithText("No announcements from your area yet").assertIsDisplayed()
    }

    @Test
    fun errorShowsMessage() {
        show(AnnouncementUiState.Error("Couldn't load announcements."))
        rule.onNodeWithText("Couldn't load announcements.").assertIsDisplayed()
    }
}
