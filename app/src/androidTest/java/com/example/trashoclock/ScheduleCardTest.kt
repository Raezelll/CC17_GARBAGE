package com.example.trashoclock

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trashoclock.ui.home.HomeScreen
import com.example.trashoclock.ui.theme.TrashOClockTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ScheduleCardTest {
    @get:Rule val rule = createComposeRule()

    @Before
    fun setUp() {
        // A fixed "today" (Wednesday) so the test doesn't depend on the real date
        rule.setContent {
            TrashOClockTheme { HomeScreen(PaddingValues(), today = LocalDate.of(2026, 10, 14)) }
        }
    }

    private fun day(name: String, date: Int) = rule.onNode(
        hasText(name) and hasText(date.toString()) and
            SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Tab)
    )

    @Test
    fun todayIsHighlightedByDefault() {
        day("Wed", 14).performScrollTo().assertIsSelected()
        day("Sun", 11).assertIsNotSelected()
        day("Sat", 17).assertIsNotSelected()
        rule.onNodeWithText("Oct 14, 2026").assertExists()
        rule.onNodeWithText("Today").assertExists()
        rule.onNodeWithText("Pick-up Point:  12 Mabini St.").assertExists()
    }

    @Test
    fun tappingDayWithoutPickUpsShowsMessage() {
        day("Fri", 16).performScrollTo().performClick()
        day("Fri", 16).assertIsSelected()
        day("Wed", 14).assertIsNotSelected()
        rule.onNodeWithText("Oct 16, 2026").assertExists()
        rule.onNodeWithText("Friday").assertExists()
        rule.onNodeWithText("No pick-ups scheduled").assertExists()
    }

    @Test
    fun tappingDayWithPickUpsShowsThem() {
        day("Mon", 12).performScrollTo().performClick()
        rule.onNodeWithText("Oct 12, 2026").assertExists()
        rule.onNodeWithText("Pick-up Point:  45 Rd. Cornelia St.").assertExists()
        rule.onNodeWithText("Pick-up Point:  45 Rd. Ronan St.").assertExists()
        rule.onNodeWithText("No pick-ups scheduled").assertDoesNotExist()

        // And back to today
        day("Wed", 14).performClick()
        rule.onNodeWithText("Today").assertExists()
    }
}
