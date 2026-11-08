package com.example.trashoclock.ui.splash

import android.provider.Settings
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.trashoclock.R
import com.example.trashoclock.ui.components.Layer
import com.example.trashoclock.ui.components.StatusBarIcons
import com.example.trashoclock.ui.theme.Green
import com.example.trashoclock.ui.theme.SplashGradientBottom
import com.example.trashoclock.ui.theme.SplashGradientMid
import com.example.trashoclock.ui.theme.SplashGradientTop
import com.example.trashoclock.ui.theme.TextMuted
import com.example.trashoclock.ui.theme.TrashOClockTheme
import com.example.trashoclock.ui.theme.poppins
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 2000L

// Figma motion on "Group 1" (74:13): -2π → 2π rad over 77.955% of a 2s loop
private const val SPIN_LOOP_MS = 2000
private const val SPIN_END_MS = 1559
private val SpinEasing = CubicBezierEasing(0.5f, 0f, 0.5f, 1f)

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    StatusBarIcons(dark = true)
    val currentOnFinished by rememberUpdatedState(onFinished)
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        currentOnFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to SplashGradientTop,
                    0.5f to SplashGradientMid,
                    1f to SplashGradientBottom,
                )
            )
    ) {
        // Background glows, bleeding off the top-right and bottom-left corners
        Image(
            painter = painterResource(R.drawable.splash_glow),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 120.dp, y = (-120).dp)
        )
        Image(
            painter = painterResource(R.drawable.splash_glow2),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-130).dp, y = 136.dp)
        )

        LogoCluster(modifier = Modifier.align(Alignment.Center))

        // Loading indicator's top edge sits 218dp below the logo's center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 218.dp)
                .requiredHeight(0.dp)
                .wrapContentHeight(Alignment.Top, unbounded = true)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                LoadingBar(width = 28.dp)
                LoadingBar(width = 44.dp)
            }
            Spacer(Modifier.height(44.dp))
            Text(text = "Loading…", style = poppins(13, FontWeight.Medium), color = TextMuted)
        }
    }
}

@Composable
private fun LoadingBar(width: Dp) {
    Box(
        Modifier
            .size(width = width, height = 6.dp)
            .background(Green, RoundedCornerShape(3.dp))
    )
}

/** Concentric rings, the spinning bin and the "signal" arcs, in a 240dp square. */
@Composable
private fun LogoCluster(modifier: Modifier = Modifier) {
    val rotation = rememberSpin()

    Box(modifier.size(240.dp)) {
        Layer(R.drawable.logo_ring, 45.dp, 45.dp)
        Layer(R.drawable.logo_r1, 51.dp, 51.dp)
        Layer(R.drawable.logo_r2, 58.5.dp, 58.5.dp)
        Layer(R.drawable.logo_r3, 69.dp, 69.dp)
        Layer(
            R.drawable.logo_bin, 93.35.dp, 74.35.dp,
            Modifier.graphicsLayer {
                rotationZ = rotation
                // Pivot on the center of the Figma frame, not the stroke-padded drawable
                transformOrigin = TransformOrigin(0.5177f, 0.5f)
            }
        )
        Layer(R.drawable.logo_arc_a, 138.5.dp, 138.5.dp)
        Layer(R.drawable.logo_arc_a, 150.5.dp, 147.5.dp)
        Layer(R.drawable.logo_arc_b, 24.5.dp, 32.5.dp)
        Layer(R.drawable.logo_arc_c, 12.5.dp, 22.5.dp)
        Layer(R.drawable.logo_arc_d, 1.5.dp, 12.5.dp)
    }
}

@Composable
private fun rememberSpin(): Float {
    val context = LocalContext.current
    val inPreview = LocalInspectionMode.current
    // Respect the system "remove animations" setting
    val animationsOff = remember {
        !inPreview && Settings.Global.getFloat(
            context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f
        ) == 0f
    }
    if (animationsOff) return 0f

    val transition = rememberInfiniteTransition(label = "splash")
    val rotation by transition.animateFloat(
        initialValue = -360f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = SPIN_LOOP_MS
                -360f at 0 using SpinEasing
                360f at SPIN_END_MS
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "binRotation",
    )
    return rotation
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun SplashScreenPreview() {
    TrashOClockTheme { SplashScreen(onFinished = {}) }
}
