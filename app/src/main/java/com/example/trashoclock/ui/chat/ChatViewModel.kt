package com.example.trashoclock.ui.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Holds the Public Chat history so it outlives the chat screen (tab switches, rotation). */
class ChatViewModel : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> get() = _messages

    fun send(text: String) {
        if (text.isBlank()) return
        _messages += ChatMessage(author = null, text = text.trim(), time = currentTime())
    }

    private fun currentTime(): String =
        SimpleDateFormat("h:mm a", Locale.US).format(Date()).lowercase(Locale.US)
}
