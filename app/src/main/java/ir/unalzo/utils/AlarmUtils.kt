package ir.unalzo.utils

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri

val Context.alarmManager: AlarmManager get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

fun Context.canScheduleExactAlarms() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    alarmManager.canScheduleExactAlarms()
} else {
    true
}

fun Context.requestExactAlarmPermission() {
    try {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = ("package:$packageName").toUri()
        }
        startActivity(intent)
    } catch (e: Exception) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        startActivity(intent)
    }
}