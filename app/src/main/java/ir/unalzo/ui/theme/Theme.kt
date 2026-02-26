package ir.unalzo.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.unalzo.di.appModule
import org.koin.compose.KoinApplication


private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xffF2F5FF)
)
private val DarkColorScheme = LightColorScheme

val screenPadding = PaddingValues(
    22.dp
)

@Composable
fun UnAlzoTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalTextStyle provides LocalTextStyle.current.copy(
                    fontFamily = vazir,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            ) {
                content()
            }
        }
    )
}

@Composable
fun Prev(
    contentPadding: Dp = 0.dp,
    content : @Composable () -> Unit
) {
    KoinApplication(
        application = {
            modules(appModule)
        }
    ) {
        UnAlzoTheme {
            Box(Modifier.background(MaterialTheme.colorScheme.background).padding(contentPadding)){
                content()
            }
        }
    }
}