package com.example.trashoclock.ui.main

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.trashoclock.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trashoclock.ui.chat.ChatScreen
import com.example.trashoclock.ui.chat.ChatViewModel
import com.example.trashoclock.ui.components.showComingSoon
import com.example.trashoclock.ui.home.HomeScreen
import com.example.trashoclock.ui.theme.FieldBorder
import com.example.trashoclock.ui.theme.GreenDark
import com.example.trashoclock.ui.theme.ShadowGreen
import com.example.trashoclock.ui.theme.TextMuted
import com.example.trashoclock.ui.theme.TrashOClockTheme
import com.example.trashoclock.ui.theme.poppins

enum class Tab { Home, Chat }

private val NavBarHeight = 80.dp

/** Home and Public Chat, sharing the bottom nav bar. */
@Composable
fun MainScreen(chatViewModel: ChatViewModel = viewModel()) {
    var tab by rememberSaveable { mutableStateOf(Tab.Home) }
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val contentPadding = PaddingValues(bottom = NavBarHeight + bottomInset)

    // Back from Public Chat returns to Home, matching the header's back arrow
    BackHandler(enabled = tab == Tab.Chat) { tab = Tab.Home }

    Box(Modifier.fillMaxSize()) {
        when (tab) {
            Tab.Home -> HomeScreen(contentPadding)
            Tab.Chat -> ChatScreen(
                messages = chatViewModel.messages,
                onSend = chatViewModel::send,
                onBack = { tab = Tab.Home },
                contentPadding = contentPadding,
            )
        }
        BottomNavBar(
            selected = tab,
            onSelect = { tab = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BottomNavBar(selected: Tab, onSelect: (Tab) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    Box(modifier.fillMaxWidth()) {
        Box(
            Modifier
                .fillMaxWidth()
                .dropShadow(shape, Shadow(radius = 7.dp, color = ShadowGreen.copy(alpha = 0.12f), offset = DpOffset(0.dp, (-3).dp)))
                .background(Color.White, shape)
                .border(1.dp, FieldBorder, shape)
                .navigationBarsPadding()
                .height(NavBarHeight)
        ) {
            // Three equal sections; the middle one stays empty under the raised "Track your Truck" button
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(NavBarHeight)
                    .padding(top = 4.dp)
            ) {
                NavItem(
                    icon = if (selected == Tab.Home) R.drawable.ic_nav_home_active else R.drawable.ic_nav_home,
                    label = "Home",
                    selected = selected == Tab.Home,
                    onClick = { onSelect(Tab.Home) },
                    modifier = Modifier.weight(1f),
                    badge = 2,
                )
                Spacer(Modifier.weight(1f))
                NavItem(
                    icon = R.drawable.ic_nav_chat,
                    label = "Public Chat",
                    selected = selected == Tab.Chat,
                    onClick = { onSelect(Tab.Chat) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
        // "Track your Truck" rises 12dp above the bar
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-12).dp)
                .size(width = 110.dp, height = 90.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable(role = Role.Button) { showComingSoon(context) }
        ) {
            Image(painterResource(R.drawable.ic_track_circle), contentDescription = null, modifier = Modifier.offset(23.dp, 0.dp))
            Image(painterResource(R.drawable.ic_truck), contentDescription = null, modifier = Modifier.offset(33.dp, 19.dp))
            Text(
                "Track your Truck",
                style = poppins(11, FontWeight.Medium),
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 68.dp)
            )
        }
    }
}

@Composable
private fun NavItem(
    @DrawableRes icon: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: Int? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(74.dp)
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(role = Role.Tab, onClick = onClick)
            .semantics { this.selected = selected }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.height(50.dp)) {
            Box {
                // Fit scales the icon down if the section is ever narrower than the drawable
                Image(
                    painterResource(icon),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
                badge?.let {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(31.dp, (-5).dp)
                    ) {
                        Image(painterResource(R.drawable.ic_badge), contentDescription = null)
                        Text(it.toString(), style = poppins(11, FontWeight.Bold), color = Color.White)
                    }
                }
            }
        }
        Text(
            label,
            style = poppins(11, if (selected) FontWeight.Bold else FontWeight.Medium),
            color = if (selected) GreenDark else TextMuted,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun MainScreenPreview() {
    TrashOClockTheme { MainScreen() }
}
