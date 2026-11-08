package com.example.trashoclock

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trashoclock.ui.main.MainScreen
import com.example.trashoclock.ui.theme.TrashOClockTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {
    @get:Rule val rule = createComposeRule()

    @Before
    fun setUp() {
        rule.setContent { TrashOClockTheme { MainScreen() } }
    }

    @Test
    fun doneShowsDroppedOffState() {
        rule.onNodeWithText("Done").performClick()
        rule.onNodeWithText("Trash dropped off").assertIsDisplayed()
    }

    // The chat header also says "Public Chat", so match nav items by role
    private fun tab(label: String) =
        rule.onNode(hasText(label) and SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Tab))

    @Test
    fun navSwitchesTabsAndChatSendsMessage() {
        tab("Home").assertIsSelected()
        tab("Public Chat").performClick()
        tab("Public Chat").assertIsSelected()
        tab("Home").assertIsNotSelected()
        rule.onNodeWithText("No messages yet. Start the conversation!").assertIsDisplayed()

        // Blank input sends nothing
        rule.onNodeWithContentDescription("Send").performClick()
        rule.onAllNodesWithText("· You", substring = true).assertCountEquals(0)

        rule.onNode(hasSetTextAction()).performTextInput("See you at the pick-up point")
        rule.onNodeWithContentDescription("Send").performClick()
        rule.onNodeWithText("See you at the pick-up point").assertIsDisplayed()
        rule.onAllNodesWithText("· You", substring = true).assertCountEquals(1)
        rule.onAllNodesWithText("No messages yet", substring = true).assertCountEquals(0)

        // Messages survive switching tabs
        tab("Home").performClick()
        rule.onNodeWithText("SCHEDULE").assertIsDisplayed()
        tab("Public Chat").performClick()
        rule.onNodeWithText("See you at the pick-up point").assertExists()
    }
}
