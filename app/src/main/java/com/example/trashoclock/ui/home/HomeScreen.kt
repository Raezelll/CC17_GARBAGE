package com.example.trashoclock.ui.home

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.trashoclock.R
import com.example.trashoclock.ui.components.StatusBarIcons
import com.example.trashoclock.ui.components.card
import com.example.trashoclock.ui.components.showComingSoon
import com.example.trashoclock.ui.theme.ChipGreen
import com.example.trashoclock.ui.theme.FieldBorder
import com.example.trashoclock.ui.theme.Green
import com.example.trashoclock.ui.theme.HeaderGreen
import com.example.trashoclock.ui.theme.ImagePlaceholderGray
import com.example.trashoclock.ui.theme.Ink
import com.example.trashoclock.ui.theme.PaleGreen
import com.example.trashoclock.ui.theme.ScheduleLine
import com.example.trashoclock.ui.theme.Scrim
import com.example.trashoclock.ui.theme.SplashGradientBottom
import com.example.trashoclock.ui.theme.SplashGradientMid
import com.example.trashoclock.ui.theme.SplashGradientTop
import com.example.trashoclock.ui.theme.TextMuted
import com.example.trashoclock.ui.theme.TrashOClockTheme
import com.example.trashoclock.ui.theme.poppins
import java.time.LocalDate
import java.util.Date

/**
 * 08 Home. [contentPadding] reserves space for the bottom nav bar drawn by the parent.
 * The announcement popup (08c) covers only this content, leaving the nav bar visible.
 * [today] is the phone's current date; tests pass a fixed one.
 * [announcements] comes live from Firestore; the preview passes sample data instead.
 */
@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now(),
    announcements: AnnouncementUiState = viewModel<AnnouncementViewModel>().state,
) {
    StatusBarIcons(dark = false)
    val context = LocalContext.current
    var droppedOff by rememberSaveable { mutableStateOf(false) }
    // The id of the announcement open in the popup, or null when it's closed
    var openAnnouncementId by rememberSaveable { mutableStateOf<String?>(null) }
    val openAnnouncement = (announcements as? AnnouncementUiState.Loaded)
        ?.announcements?.find { it.id == openAnnouncementId }

    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(0f to SplashGradientTop, 0.5f to SplashGradientMid, 1f to SplashGradientBottom))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            Header(onSettings = { showComingSoon(context) })
            Spacer(Modifier.height(10.dp))
            ReminderCard(droppedOff = droppedOff, onDone = { droppedOff = true })
            SectionTitle(R.drawable.img_megaphone, "ANNOUNCEMENT", iconSize = 51.66.dp, iconStart = 16.dp, titleStart = 62.dp, titleTop = 12.dp, iconRotation = -0.94f)
            AnnouncementSection(announcements, onOpen = { openAnnouncementId = it.id })
            SectionTitle(R.drawable.img_schedule, "SCHEDULE", iconSize = 48.dp, iconStart = 13.dp, titleStart = 65.dp, titleTop = 15.dp)
            ScheduleCard(today = today, onPickUpClick = { showComingSoon(context) })
            Spacer(Modifier.height(10.dp))
        }

        if (openAnnouncement != null) {
            AnnouncementPopup(
                announcement = openAnnouncement,
                onDismiss = { openAnnouncementId = null },
                modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding())
            )
        }
    }
}

@Composable
private fun Header(onSettings: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(HeaderGreen, RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
    ) {
        Image(painterResource(R.drawable.ic_location), contentDescription = null, modifier = Modifier.offset(20.dp, 50.dp))
        Column(Modifier.offset(46.dp, 46.dp)) {
            Text("123 Street", style = poppins(16, FontWeight.Bold), color = PaleGreen)
            Text("City, Province", style = poppins(13), color = PaleGreen, modifier = Modifier.offset(y = (-4).dp))
        }
        Image(
            painter = painterResource(R.drawable.ic_settings),
            contentDescription = "Settings",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-20).dp, y = 44.dp)
                .clip(RoundedCornerShape(22.dp))
                .clickable(onClick = onSettings)
        )
    }
}

/** 08 "Drop off your trash today!" / 08b "Trash dropped off". */
@Composable
private fun ReminderCard(droppedOff: Boolean, onDone: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(96.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .card(radius = 22.dp, shadowBlur = 7.dp)
        ) {
            if (droppedOff) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(start = 17.dp, top = 31.dp)
                ) {
                    Text("Trash dropped off", style = poppins(22, FontWeight.Bold), color = Ink)
                    Image(painterResource(R.drawable.ic_check_circle), contentDescription = null)
                }
            } else {
                Text(
                    "Drop off your  trash today!",
                    style = poppins(21, FontWeight.Bold).copy(lineHeight = 25.sp),
                    color = Ink,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.padding(start = 14.dp, top = 21.dp)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 50.dp)
                        .size(width = 84.dp, height = 28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Green)
                        .clickable(onClick = onDone)
                ) {
                    Text("Done", style = poppins(13, FontWeight.Medium), color = Color.White)
                }
            }
        }
        // The bag photo hangs 15dp past the card's right edge
        Image(
            painter = painterResource(R.drawable.img_trash_bag),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 15.dp, y = if (droppedOff) 0.dp else (-1).dp)
                .size(98.dp)
                .clip(RoundedCornerShape(44.dp))
        )
    }
}

/** Icon + bold heading in the 42dp gap between cards. */
@Composable
private fun SectionTitle(
    @DrawableRes icon: Int,
    title: String,
    iconSize: Dp,
    iconStart: Dp,
    titleStart: Dp,
    titleTop: Dp,
    iconRotation: Float = 0f,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(42.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .offset(x = iconStart)
                .size(iconSize)
                .rotate(iconRotation)
        )
        Text(title, style = poppins(18, FontWeight.Bold), color = Ink, modifier = Modifier.offset(titleStart, titleTop))
    }
}

/** The Firestore announcements, newest first, one card each; or a single card saying why there are none. */
@Composable
private fun AnnouncementSection(state: AnnouncementUiState, onOpen: (Announcement) -> Unit) {
    when (state) {
        AnnouncementUiState.Loading -> AnnouncementStatusCard {
            CircularProgressIndicator(color = Green, strokeWidth = 3.dp, modifier = Modifier.size(28.dp))
        }
        is AnnouncementUiState.Error -> AnnouncementStatusCard {
            Text(state.message, style = poppins(14, FontWeight.Medium), color = TextMuted, textAlign = TextAlign.Center)
        }
        is AnnouncementUiState.Loaded -> if (state.announcements.isEmpty()) {
            AnnouncementStatusCard {
                Text("No announcements from your area yet", style = poppins(14, FontWeight.Medium), color = TextMuted, textAlign = TextAlign.Center)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.announcements.forEach { announcement ->
                    AnnouncementCard(announcement, onClick = { onOpen(announcement) })
                }
            }
        }
    }
}

@Composable
private fun AnnouncementCard(announcement: Announcement, onClick: () -> Unit) {
    val imageUrl = announcement.imageUrl?.takeIf { it.isNotBlank() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            // Cards with a picture grow by exactly the picture and the gap under it
            .height(if (imageUrl != null) 120.dp + AnnouncementImageHeight + 6.dp else 120.dp)
            .card(radius = 22.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(top = 5.dp, bottom = 4.dp, start = 14.dp, end = 14.dp)
    ) {
        Text(
            announcement.title,
            style = poppins(12, FontWeight.Bold).copy(fontSize = 12.5.sp),
            color = Ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        postedLine(announcement)?.let { Text(it, style = poppins(11), color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        Spacer(Modifier.height(2.dp))
        if (imageUrl != null) {
            AnnouncementImage(imageUrl)
            Spacer(Modifier.height(6.dp))
        }
        // Two lines, leaving room for the arrow under "View more"; the popup has the full text
        Text(
            announcement.message,
            style = poppins(11),
            color = Ink,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("View more", style = poppins(11, FontWeight.SemiBold), color = Ink)
            Text("⌄", style = poppins(11, FontWeight.Medium).copy(lineHeight = 11.sp), color = Ink)
        }
    }
}

private val AnnouncementImageHeight = 180.dp

/** The announcement's picture; plain gray while it loads, and if it can't be loaded. */
@Composable
private fun AnnouncementImage(url: String) {
    val gray = remember { ColorPainter(ImagePlaceholderGray) }
    AsyncImage(
        model = url,
        contentDescription = null,
        placeholder = gray,
        error = gray,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(AnnouncementImageHeight)
            .clip(RoundedCornerShape(14.dp))
    )
}

/** Same card shape as [AnnouncementCard], with [content] centered: used for loading, empty and error. */
@Composable
private fun AnnouncementStatusCard(content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(120.dp)
            .card(radius = 22.dp)
            .padding(horizontal = 20.dp)
    ) {
        content()
    }
}

/** "Posted by Barangay Hall · Nov 7, 2026", leaving out whichever part is missing; null if both are. */
private fun postedLine(announcement: Announcement): String? =
    listOfNotNull(
        announcement.postedBy?.let { "Posted by $it" },
        announcement.datePosted?.let(::formatAnnouncementDate),
    ).joinToString(" · ").ifEmpty { null }

@Composable
private fun ScheduleCard(today: LocalDate, onPickUpClick: () -> Unit) {
    // The tapped day, stored as a day number so it survives rotation; starts on today.
    // Keyed on today so it resets if the app is still open when the date changes.
    var selectedDay by rememberSaveable(today) { mutableStateOf(today.toEpochDay()) }
    val selected = LocalDate.ofEpochDay(selectedDay)
    val pickUps = pickUpsOn(selected)

    Box(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(340.dp)
            .card(radius = 22.dp)
            .clip(RoundedCornerShape(22.dp))
    ) {
        Text(fullDate(selected), style = poppins(12), color = TextMuted, modifier = Modifier.offset(21.dp, 11.dp))
        Text(dayTitle(selected, today), style = poppins(22, FontWeight.Bold), color = Ink, modifier = Modifier.offset(21.dp, 27.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 65.dp)
        ) {
            weekOf(today).forEach { day ->
                DayChip(day, selected = day == selected, onClick = { selectedDay = day.toEpochDay() })
            }
        }

        if (pickUps.isEmpty()) {
            Text(
                "No pick-ups scheduled",
                style = poppins(14, FontWeight.Medium),
                color = TextMuted,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 70.dp)
            )
        } else {
            ScheduleHours.forEachIndexed { i, hour ->
                val rowTop = 146.dp + 30.dp * i
                Text(hour, style = poppins(12), color = TextMuted, modifier = Modifier.offset(19.dp, rowTop + 3.dp))
                Box(
                    Modifier
                        .offset(y = rowTop + 11.5.dp)
                        .padding(start = 91.dp, end = 19.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ScheduleLine, RoundedCornerShape(0.5.dp))
                )
                pickUps.find { it.hourIndex == i }?.let { pickUp ->
                    PickUpChip(
                        "Pick-up Point:  ${pickUp.place}",
                        onPickUpClick,
                        Modifier.offset(y = rowTop).padding(start = 91.dp, end = 19.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DayChip(day: LocalDate, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(width = 46.dp, height = 62.dp)
            .clip(shape)
            .clickable(role = Role.Tab, onClick = onClick)
            .semantics { this.selected = selected }
            .then(
                if (selected) Modifier.background(HeaderGreen, shape)
                else Modifier.background(Color.White, shape).border(1.dp, FieldBorder, shape)
            )
            .padding(top = if (selected) 8.dp else 7.dp)
    ) {
        Text(shortDayName(day), style = poppins(12), color = TextMuted)
        Text(
            day.dayOfMonth.toString(),
            style = poppins(15, FontWeight.Bold),
            color = if (selected) Color.White else TextMuted,
            modifier = Modifier.offset(y = 1.dp)
        )
    }
}

@Composable
private fun PickUpChip(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(ChipGreen)
            .clickable(onClick = onClick)
            .padding(start = 8.dp)
    ) {
        Image(painterResource(R.drawable.ic_pin_small), contentDescription = null)
        Spacer(Modifier.width(6.dp))
        Text(label, style = poppins(11, FontWeight.Bold), color = Ink, maxLines = 1)
    }
}

/** 08c: the full announcement over a dimmed Home. */
@Composable
private fun AnnouncementPopup(announcement: Announcement, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    BackHandler(onBack = onDismiss)
    Box(
        modifier
            .fillMaxSize()
            .background(Scrim.copy(alpha = 0.7f))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onDismiss)
            .padding(top = 110.dp, bottom = 34.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                // 620dp as designed, shrinking (and scrolling) on short screens
                .heightIn(max = 620.dp)
                .fillMaxHeight()
                .card(radius = 28.dp)
                .clip(RoundedCornerShape(28.dp))
                // Swallow taps so only the scrim dismisses
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 29.dp, bottom = 24.dp)
            ) {
                Text("ANNOUNCEMENT", style = poppins(24, FontWeight.Bold), color = Ink)
                Spacer(Modifier.height(6.dp))
                Text(
                    announcement.title,
                    style = poppins(15, FontWeight.Bold),
                    color = Ink,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 19.dp)
                )
                postedLine(announcement)?.let { Text(it, style = poppins(12), color = Ink, textAlign = TextAlign.Center) }
                Spacer(Modifier.height(20.dp))
                Text(
                    announcement.message,
                    style = poppins(12).copy(lineHeight = 18.sp),
                    color = Ink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp)
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-15).dp, y = 9.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(onClick = onDismiss)
            )
        }
    }
}

// The preview can't reach Firestore, so it shows a sample announcement
@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenPreview() {
    val sample = Announcement(
        id = "preview",
        title = "Delay in Garbage Collection for next week",
        message = "Due to Typhoon Pepito, garbage collection services have been delayed.",
        postedBy = "Trash o’ clock Team",
        datePosted = Date(),
        imageUrl = null,
    )
    TrashOClockTheme {
        HomeScreen(
            contentPadding = PaddingValues(bottom = 80.dp),
            announcements = AnnouncementUiState.Loaded(listOf(sample)),
        )
    }
}
