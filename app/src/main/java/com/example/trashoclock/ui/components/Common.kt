package com.example.trashoclock.ui.components

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.trashoclock.ui.theme.FieldBorder
import com.example.trashoclock.ui.theme.ShadowGreen

/** A decorative drawable placed at a fixed Figma offset inside its parent Box. */
@Composable
fun Layer(@DrawableRes id: Int, x: Dp, y: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id),
        contentDescription = null,
        modifier = Modifier.offset(x, y).then(modifier)
    )
}

/** Sets whether the status bar icons are dark (for light backgrounds) or light. */
@Composable
fun StatusBarIcons(dark: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = dark
    }
}

/**
 * The design's white card: #E1E9DA border, rounded corners and a soft green-tinted
 * drop shadow (Figma: y-offset [shadowY], blur [shadowBlur], rgba(33,64,26,[shadowAlpha])).
 */
fun Modifier.card(
    radius: Dp,
    shadowBlur: Dp = 14.dp,
    shadowY: Dp = 4.dp,
    shadowAlpha: Float = 0.1f,
    color: Color = Color.White,
): Modifier {
    val shape = RoundedCornerShape(radius)
    return this
        .dropShadow(shape, Shadow(radius = shadowBlur, color = ShadowGreen.copy(alpha = shadowAlpha), offset = DpOffset(0.dp, shadowY)))
        .background(color, shape)
        .border(1.dp, FieldBorder, shape)
}

fun showComingSoon(context: Context) {
    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
}
