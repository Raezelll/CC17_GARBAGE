package com.example.trashoclock.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trashoclock.R
import com.example.trashoclock.ui.components.Layer
import com.example.trashoclock.ui.components.StatusBarIcons
import com.example.trashoclock.ui.components.showComingSoon
import com.example.trashoclock.ui.theme.Blush
import com.example.trashoclock.ui.theme.Green
import com.example.trashoclock.ui.theme.HeaderGreen
import com.example.trashoclock.ui.theme.LinkBlue
import com.example.trashoclock.ui.theme.Mint
import com.example.trashoclock.ui.theme.PaleGreen
import com.example.trashoclock.ui.theme.Pink
import com.example.trashoclock.ui.theme.TextDark
import com.example.trashoclock.ui.theme.TrashOClockTheme
import com.example.trashoclock.ui.theme.poppins

@Composable
fun WelcomeScreen(onSignUp: () -> Unit) {
    StatusBarIcons(dark = false)
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Mint)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Header()
        Spacer(Modifier.height(50.dp))
        WelcomeLogo()
        Spacer(Modifier.height(70.dp))
        Button(
            onClick = onSignUp,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = Color.White),
            modifier = Modifier.size(width = 290.dp, height = 56.dp)
        ) {
            Text("SIGN UP", style = poppins(22, FontWeight.Medium))
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
private fun Header() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 230.dp)
            .background(HeaderGreen, RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
            .padding(top = 64.dp, bottom = 4.dp)
    ) {
        Text("Welcome to", style = poppins(26, FontWeight.Bold), color = PaleGreen)
        Text("TRASH O’ CLOCK", style = poppins(32, FontWeight.Bold), color = Pink)
        Text(
            "Bin there, done that.\nNever have piled up garbage and missed truck again with your garbage collection companion!",
            style = poppins(13).copy(lineHeight = 19.sp),
            color = PaleGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(300.dp)
        )
    }
}

/** Rings, bin and arcs in a 240dp square (Figma origin 75, 280). */
@Composable
private fun WelcomeLogo() {
    Box(Modifier.size(240.dp)) {
        Layer(R.drawable.logo_ring, 45.dp, 45.dp)
        Layer(R.drawable.logo_r1, 51.dp, 51.dp)
        Layer(R.drawable.logo_r2, 58.5.dp, 58.5.dp)
        Layer(R.drawable.logo_r3, 69.dp, 69.dp)
        Slat(114.dp)
        Slat(129.dp)
        Layer(R.drawable.logo_bin, 95.6.dp, 72.01.dp)
        Layer(R.drawable.logo_arc_b, 29.63.dp, 29.63.dp)
        Layer(R.drawable.logo_arc_c, 16.11.dp, 16.11.dp)
        Layer(R.drawable.logo_arc_d, 2.59.dp, 2.59.dp)
        Layer(R.drawable.logo_arc_e, 142.31.dp, 142.31.dp)
        Layer(R.drawable.logo_arc_a, 145.93.dp, 145.93.dp)
    }
}

@Composable
private fun Slat(x: Dp) {
    Box(
        Modifier
            .offset(x, 126.dp)
            .size(width = 2.4.dp, height = 24.dp)
            .background(Blush, RoundedCornerShape(1.dp))
    )
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun WelcomeScreenPreview() {
    TrashOClockTheme { WelcomeScreen(onSignUp = {}) }
}
