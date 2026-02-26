package ir.unalzo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.unalzo.R

val vazir = FontFamily(
    listOf(
        Font(
            resId = R.font.vazir_thin,
            weight = FontWeight.Thin
        ),
        Font(
            resId = R.font.vazir_normal,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.vazir_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.vazir_bold,
            weight = FontWeight.Bold
        ),
    )
)
val poppins = FontFamily(
    listOf(
        Font(
            resId = R.font.poppins_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.poppins_black,
            weight = FontWeight.Black
        ),
    )
)

val defaultStyle = TextStyle(
    fontFamily = vazir,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
)
val Typography = Typography(
    displayLarge = defaultStyle,
    displayMedium = defaultStyle,
    displaySmall = defaultStyle,
    headlineLarge = defaultStyle,
    headlineMedium = defaultStyle,
    headlineSmall = defaultStyle,
    titleLarge = defaultStyle,
    titleMedium = defaultStyle,
    titleSmall = defaultStyle,
    bodyLarge = defaultStyle,
    bodyMedium = defaultStyle,
    bodySmall = defaultStyle,
    labelLarge = defaultStyle,
    labelMedium = defaultStyle,
    labelSmall = defaultStyle,
)
