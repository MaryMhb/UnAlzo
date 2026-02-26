package ir.unalzo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import ir.unalzo.navigation.container.RootNav
import ir.unalzo.ui.theme.UnAlzoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = TRANSPARENT,
                darkScrim = TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = TRANSPARENT,
                darkScrim = TRANSPARENT
            )
        )
        setContent {
            UnAlzoTheme {
                Box(Modifier.background(MaterialTheme.colorScheme.background)) {
                    RootNav()
                }
            }
        }
    }
}

fun createNotificationChannel(context: Context) {
    val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    service.createNotificationChannel(
        NotificationChannel(
            "channel",
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_MAX
        )
    )
}