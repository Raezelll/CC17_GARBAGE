package com.example.trashoclock.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.trashoclock.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
)

private val Default = Typography()

// Every Material text style uses Poppins, matching the design
val Typography = Typography(
    displayLarge = Default.displayLarge.copy(fontFamily = Poppins),
    displayMedium = Default.displayMedium.copy(fontFamily = Poppins),
    displaySmall = Default.displaySmall.copy(fontFamily = Poppins),
    headlineLarge = Default.headlineLarge.copy(fontFamily = Poppins),
    headlineMedium = Default.headlineMedium.copy(fontFamily = Poppins),
    headlineSmall = Default.headlineSmall.copy(fontFamily = Poppins),
    titleLarge = Default.titleLarge.copy(fontFamily = Poppins),
    titleMedium = Default.titleMedium.copy(fontFamily = Poppins),
    titleSmall = Default.titleSmall.copy(fontFamily = Poppins),
    bodyLarge = Default.bodyLarge.copy(fontFamily = Poppins),
    bodyMedium = Default.bodyMedium.copy(fontFamily = Poppins),
    bodySmall = Default.bodySmall.copy(fontFamily = Poppins),
    labelLarge = Default.labelLarge.copy(fontFamily = Poppins),
    labelMedium = Default.labelMedium.copy(fontFamily = Poppins),
    labelSmall = Default.labelSmall.copy(fontFamily = Poppins),
)

/** Poppins at an exact Figma size/weight, with Figma's "normal" line height. */
fun poppins(size: Int, weight: FontWeight = FontWeight.Normal) =
    TextStyle(fontFamily = Poppins, fontSize = size.sp, fontWeight = weight)
