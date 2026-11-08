package com.example.trashoclock.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

/**
 * Listens to the Firestore "Announcements" collection, newest first. Firestore calls the
 * listener again whenever a document is added, edited or deleted, so Home updates live.
 */
class AnnouncementViewModel : ViewModel() {
    var state by mutableStateOf<AnnouncementUiState>(AnnouncementUiState.Loading)
        private set

    private val registration: ListenerRegistration = FirebaseFirestore.getInstance()
        .collection("Announcements")
        .orderBy("DatePosted", Query.Direction.DESCENDING)
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Couldn't load announcements", error)
                state = AnnouncementUiState.Error("Couldn't load announcements. Please try again later.")
                return@addSnapshotListener
            }
            // The field names in Firestore are capitalized, so they're read one by one
            val announcements = snapshot?.documents.orEmpty().map { doc ->
                Announcement(
                    id = doc.id,
                    title = doc.getString("Title").orEmpty(),
                    message = doc.getString("Message").orEmpty(),
                    postedBy = doc.getString("PostedBy")?.takeIf { it.isNotBlank() },
                    datePosted = doc.getTimestamp("DatePosted")?.toDate(),
                    imageUrl = doc.getString("ImageUrl")?.takeIf { it.isNotBlank() },
                )
            }
            state = AnnouncementUiState.Loaded(announcements)
        }

    override fun onCleared() {
        registration.remove()
    }

    private companion object {
        const val TAG = "AnnouncementViewModel"
    }
}
