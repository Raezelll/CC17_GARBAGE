package com.example.trashoclock

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trashoclock.ui.signup.SignUpScreen
import com.example.trashoclock.ui.theme.TrashOClockTheme
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {
    @get:Rule val rule = createComposeRule()

    private var signedUp = false

    @Before
    fun setUp() {
        rule.setContent {
            TrashOClockTheme { SignUpScreen(onBack = {}, onSignedUp = { signedUp = true }) }
        }
    }

    // Text fields in screen order: phone, address, password, confirm
    private fun field(index: Int) = rule.onAllNodes(hasSetTextAction())[index]
    private fun signUpButton() = rule.onNode(hasText("SIGN UP") and hasClickAction())

    private fun fillValidForm() {
        field(0).performTextInput("9171234567")
        field(1).performTextInput("Manila")
        field(2).performTextInput("Trash*123")
        field(3).performTextInput("Trash*123")
    }

    @Test
    fun buttonDisabledUntilFormIsValid() {
        signUpButton().assertIsNotEnabled()
        fillValidForm()
        // The on-screen keyboard covers the bottom of the form, so scroll like a user would
        signUpButton().performScrollTo().assertIsEnabled().performClick()
        assertTrue(signedUp)
    }

    @Test
    fun passwordRulesUpdateLive() {
        rule.onNodeWithText("Use at least 8 characters", substring = true).assertIsDisplayed()
        field(2).performTextInput("trash")
        rule.onNodeWithText("At least 8 characters").assertIsDisplayed()
        rule.onAllNodesWithText("✓").assertCountEquals(1) // lowercase only
        rule.onAllNodesWithText("✗").assertCountEquals(3)
        field(2).performTextReplacement("Trash*123")
        rule.onAllNodesWithText("✓").assertCountEquals(4)
    }

    @Test
    fun mismatchedConfirmShowsErrorAndDisablesButton() {
        fillValidForm()
        field(3).performTextReplacement("Trash*12")
        rule.onNodeWithText("Passwords don't match").performScrollTo().assertIsDisplayed()
        signUpButton().assertIsNotEnabled()
        field(3).performTextReplacement("Trash*123")
        rule.onNodeWithText("Passwords don't match").assertDoesNotExist()
        signUpButton().assertIsEnabled()
    }

    @Test
    fun weakPasswordKeepsButtonDisabled() {
        field(0).performTextInput("9171234567")
        field(1).performTextInput("Manila")
        field(2).performTextInput("trash*123") // no uppercase
        field(3).performTextInput("trash*123")
        signUpButton().assertIsNotEnabled()
    }

    @Test
    fun phoneAcceptsOnlyTenDigits() {
        field(0).performTextInput("91a7-123456789")
        rule.onNodeWithText("9171234567").assertIsDisplayed()
    }

    @Test
    fun eyeTogglesPasswordVisibility() {
        rule.onAllNodesWithContentDescription("Show password").assertCountEquals(2)
        rule.onAllNodesWithContentDescription("Show password")[0].performClick()
        // Only the password field's toggle changes; confirm stays hidden
        rule.onNodeWithContentDescription("Hide password").assertIsDisplayed()
        rule.onAllNodesWithContentDescription("Show password").assertCountEquals(1)
    }
}
