package com.example.trashoclock.ui.signup

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trashoclock.R
import com.example.trashoclock.ui.components.StatusBarIcons
import com.example.trashoclock.ui.components.showComingSoon
import com.example.trashoclock.ui.theme.CheckboxLabel
import com.example.trashoclock.ui.theme.ErrorRed
import com.example.trashoclock.ui.theme.FieldBorder
import com.example.trashoclock.ui.theme.FieldText
import com.example.trashoclock.ui.theme.Green
import com.example.trashoclock.ui.theme.HeaderGreen
import com.example.trashoclock.ui.theme.HintText
import com.example.trashoclock.ui.theme.LabelText
import com.example.trashoclock.ui.theme.LinkBlue
import com.example.trashoclock.ui.theme.Mint
import com.example.trashoclock.ui.theme.TextDark
import com.example.trashoclock.ui.theme.TrashOClockTheme
import com.example.trashoclock.ui.theme.poppins

private const val PHONE_DIGITS = 10 // Philippine mobile number after +63

/** The live password rules, in the order the design's hint lists them. */
private val PasswordRules: List<Pair<String, (String) -> Boolean>> = listOf(
    "At least 8 characters" to { it.length >= 8 },
    "One uppercase letter" to { p -> p.any { it.isUpperCase() } },
    "One lowercase letter" to { p -> p.any { it.isLowerCase() } },
    "One special character (e.g. *)" to { p -> p.any { !it.isLetterOrDigit() && !it.isWhitespace() } },
)

@Composable
fun SignUpScreen(onBack: () -> Unit, onSignedUp: () -> Unit) {
    StatusBarIcons(dark = false)
    val context = LocalContext.current

    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirm by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    val passwordValid = PasswordRules.all { (_, check) -> check(password) }
    val confirmMismatch = confirm.isNotEmpty() && confirm != password
    val formValid = phone.length == PHONE_DIGITS && address.isNotBlank() &&
        passwordValid && confirm == password

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Mint)
            // Shrink the viewport above the keyboard so the focused field scrolls into view
            .imePadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Header(onBack)
        Spacer(Modifier.height(21.dp))
        Text("SIGN UP", style = poppins(24, FontWeight.Medium), color = TextDark)

        Column(Modifier.width(310.dp)) {
            FieldLabel("Phone Number")
            InputField(
                value = phone,
                onValueChange = { new -> phone = new.filter(Char::isDigit).take(PHONE_DIGITS) },
                prefix = "+63",
                keyboardType = KeyboardType.Phone,
            )
            Spacer(Modifier.height(12.dp))

            FieldLabel("Address")
            InputField(
                value = address,
                onValueChange = { address = it },
                leadingIcon = R.drawable.ic_pin,
                keyboardType = KeyboardType.Text,
            )
            Spacer(Modifier.height(12.dp))

            FieldLabel("Password")
            PasswordField(password, { password = it }, showPassword) { showPassword = !showPassword }
            Spacer(Modifier.height(4.dp))
            PasswordHint(password)
            Spacer(Modifier.height(6.dp))

            FieldLabel("Confirm Password")
            PasswordField(confirm, { confirm = it }, showConfirm, isLast = true) { showConfirm = !showConfirm }
            if (confirmMismatch) {
                Text(
                    "Passwords don't match",
                    style = poppins(11).copy(lineHeight = 15.sp),
                    color = ErrorRed,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(Modifier.height(22.dp))

            RememberMe(checked = rememberMe, onToggle = { rememberMe = !rememberMe })
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onSignedUp,
            enabled = formValid,
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green,
                contentColor = Color.White,
                disabledContainerColor = Green.copy(alpha = 0.4f),
                disabledContentColor = Color.White.copy(alpha = 0.8f),
            ),
            modifier = Modifier.size(width = 200.dp, height = 50.dp)
        ) {
            Text("SIGN UP", style = poppins(17, FontWeight.Medium))
        }
        Spacer(Modifier.height(18.dp))
        Text("Already have an Account?", style = poppins(13), color = TextDark)
        Text(
            "Login",
            style = poppins(14, FontWeight.Medium),
            color = LinkBlue,
            modifier = Modifier.clickable { showComingSoon(context) }
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Header(onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(175.dp)
            .background(HeaderGreen, RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .offset(12.dp, 42.dp)
                .clip(RoundedCornerShape(22.dp))
                .clickable(onClick = onBack)
        )
        Image(
            painter = painterResource(R.drawable.logo_small),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 49.dp)
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(text, style = poppins(13, FontWeight.Medium), color = LabelText)
}

/** White, bordered single-line field from the design (310 × 44, radius 10). */
@Composable
private fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    prefix: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next,
    trailing: (@Composable () -> Unit)? = null,
) {
    val textStyle = poppins(14).copy(color = FieldText)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle,
        cursorBrush = SolidColor(Green),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .border(1.dp, FieldBorder, RoundedCornerShape(10.dp))
                    .padding(start = if (leadingIcon == null) 13.dp else 0.dp)
            ) {
                // Icons are 44 × 44 and keep their exact Figma position inside the field
                leadingIcon?.let { Image(painterResource(it), contentDescription = null) }
                prefix?.let { Text("$it ", style = textStyle) }
                Box(Modifier.weight(1f)) { innerTextField() }
                trailing?.invoke()
            }
        },
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    isLast: Boolean = false,
    onToggleVisible: () -> Unit,
) {
    InputField(
        value = value,
        onValueChange = onValueChange,
        keyboardType = KeyboardType.Password,
        leadingIcon = R.drawable.ic_lock,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
        trailing = {
            Image(
                painter = painterResource(if (visible) R.drawable.ic_eye else R.drawable.ic_eye_off),
                contentDescription = if (visible) "Hide password" else "Show password",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .clickable(onClick = onToggleVisible)
            )
        },
    )
}

/**
 * The design's hint while the password is empty; once typing starts,
 * each rule is listed and turns green when it's met.
 */
@Composable
private fun PasswordHint(password: String) {
    val style = poppins(11).copy(lineHeight = 15.sp)
    if (password.isEmpty()) {
        Text(
            "Use at least 8 characters, one uppercase, one lowercase and one special character (e.g. *)",
            style = style,
            color = HintText,
        )
        return
    }
    Column {
        PasswordRules.forEach { (label, check) ->
            val color = if (check(password)) Green else ErrorRed
            Row {
                // Fixed-width marker column so ✓ and ✗ labels line up
                Text(if (check(password)) "✓" else "✗", style = style, color = color, modifier = Modifier.width(14.dp))
                Text(label, style = style, color = color)
            }
        }
    }
}

@Composable
private fun RememberMe(checked: Boolean, onToggle: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.clickable(role = Role.Checkbox, onClick = onToggle)
    ) {
        Box(
            Modifier
                .size(22.dp)
                .background(if (checked) Green else Color.White, RoundedCornerShape(4.dp))
                .border(1.dp, Green, RoundedCornerShape(4.dp))
        ) {
            if (checked) {
                Canvas(Modifier.fillMaxSize()) {
                    val w = size.width
                    val stroke = 2.dp.toPx()
                    drawLine(Color.White, Offset(w * 0.25f, w * 0.52f), Offset(w * 0.43f, w * 0.7f), stroke, StrokeCap.Round)
                    drawLine(Color.White, Offset(w * 0.43f, w * 0.7f), Offset(w * 0.76f, w * 0.32f), stroke, StrokeCap.Round)
                }
            }
        }
        Text("Remember me?", style = poppins(14), color = CheckboxLabel)
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun SignUpScreenPreview() {
    TrashOClockTheme { SignUpScreen(onBack = {}, onSignedUp = {}) }
}
