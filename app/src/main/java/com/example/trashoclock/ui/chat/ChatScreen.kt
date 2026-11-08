package com.example.trashoclock.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trashoclock.R
import com.example.trashoclock.ui.components.StatusBarIcons
import com.example.trashoclock.ui.components.card
import com.example.trashoclock.ui.theme.ChatText
import com.example.trashoclock.ui.theme.FieldBorder
import com.example.trashoclock.ui.theme.Green
import com.example.trashoclock.ui.theme.HeaderGreen
import com.example.trashoclock.ui.theme.Ink
import com.example.trashoclock.ui.theme.Mint
import com.example.trashoclock.ui.theme.Placeholder
import com.example.trashoclock.ui.theme.TextMuted
import com.example.trashoclock.ui.theme.TrashOClockTheme
import com.example.trashoclock.ui.theme.poppins

/** A chat message; [author] is null for messages sent by "You". */
data class ChatMessage(val author: String?, val text: String, val time: String? = null)

private val Avatars = mapOf(
    "Jane Doe" to R.drawable.img_avatar_jane,
    "Maria Mary" to R.drawable.img_avatar_maria,
    "John Dough" to R.drawable.img_avatar_john,
)

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    onSend: (String) -> Unit,
    onBack: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    StatusBarIcons(dark = false)
    var draft by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    fun send() {
        if (draft.isBlank()) return
        onSend(draft)
        draft = ""
        // Keep the cursor in the box and the keyboard open for the next message
        focusRequester.requestFocus()
        keyboard?.show()
    }

    // After sending, scroll to the newest message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    // While the keyboard slides open it shrinks the list from below; keep the newest
    // message pinned in view on every step of that animation
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    LaunchedEffect(keyboardHeight) {
        if (keyboardHeight > 0 && messages.isNotEmpty()) listState.scrollToItem(messages.lastIndex)
    }

    Column(
        modifier
            .fillMaxSize()
            .background(Mint)
            .padding(contentPadding)
            // Above the keyboard when it's open; the nav bar space above already counts toward it
            .consumeWindowInsets(contentPadding)
            .imePadding()
    ) {
        Header(onBack)
        if (messages.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    "No messages yet. Start the conversation!",
                    style = poppins(13),
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(start = 16.dp, end = 20.dp, top = 62.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(messages) { message ->
                    if (message.author == null) MyMessage(message) else TheirMessage(message)
                }
            }
        }
        InputBar(value = draft, onValueChange = { draft = it }, onSend = ::send, focusRequester = focusRequester)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Header(onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(HeaderGreen, RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Back to Home",
            modifier = Modifier
                .offset(12.dp, 56.dp)
                .clip(RoundedCornerShape(22.dp))
                .clickable(onClick = onBack)
        )
        Text("Public Chat", style = poppins(20, FontWeight.Medium), color = Color.White, modifier = Modifier.offset(60.dp, 60.dp))
    }
}

@Composable
private fun TheirMessage(message: ChatMessage) {
    Row {
        Image(
            painter = painterResource(Avatars[message.author] ?: R.drawable.img_avatar_jane),
            contentDescription = null,
            modifier = Modifier.padding(top = 14.dp)
        )
        Spacer(Modifier.size(20.dp))
        Column {
            Text(message.author.orEmpty(), style = poppins(12), color = Ink)
            Text(
                message.text,
                style = poppins(13).copy(lineHeight = 18.sp),
                color = ChatText,
                modifier = Modifier
                    .widthIn(max = 218.dp)
                    .card(radius = 14.dp, shadowBlur = 7.dp)
                    // Figma's text box is 206 wide in a 218 bubble: 11 start, 1 end
                    .padding(start = 11.dp, end = 1.dp, top = 8.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun MyMessage(message: ChatMessage) {
    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
        Text(
            message.text,
            style = poppins(13).copy(lineHeight = 18.sp),
            color = Color.White,
            modifier = Modifier
                .widthIn(max = 260.dp)
                .card(radius = 14.dp, shadowBlur = 7.dp, color = Green)
                .padding(horizontal = 11.dp, vertical = 8.dp)
        )
        message.time?.let {
            Text("$it · You", style = poppins(10), color = TextMuted, modifier = Modifier.padding(top = 5.dp))
        }
    }
}

@Composable
private fun InputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    focusRequester: FocusRequester,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(start = 15.dp, end = 17.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = poppins(14).copy(color = Ink),
            cursorBrush = SolidColor(Green),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            // The whole rounded box (including the emoji dot) is the text field, so a tap
            // anywhere on it focuses the field and opens the keyboard
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            decorationBox = { inner ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(46.dp)
                        .background(Color.White, RoundedCornerShape(23.dp))
                        .border(1.dp, FieldBorder, RoundedCornerShape(23.dp))
                        .padding(start = 19.dp, end = 13.dp)
                ) {
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty()) Text("Aa", style = poppins(14), color = Placeholder)
                        inner()
                    }
                    Image(painterResource(R.drawable.ic_emoji), contentDescription = null)
                }
            },
        )
        Image(
            painter = painterResource(R.drawable.ic_send),
            contentDescription = "Send",
            modifier = Modifier
                .clip(RoundedCornerShape(23.dp))
                .clickable(onClick = onSend)
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun ChatScreenPreview() {
    TrashOClockTheme {
        ChatScreen(
            messages = listOf(ChatMessage(null, "Hello!", "9:45 am")),
            onSend = {},
            onBack = {},
            contentPadding = PaddingValues(bottom = 80.dp),
        )
    }
}
