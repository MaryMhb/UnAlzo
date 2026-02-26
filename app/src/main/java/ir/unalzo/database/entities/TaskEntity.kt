package ir.unalzo.database.entities

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aminography.primecalendar.persian.PersianCalendar
import ir.unalzo.ReminderReceiver
import ir.unalzo.utils.alarmManager

@Entity("tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id: Int = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val isDone: Boolean = false,
    @ColumnInfo val reminderTime: Long?,
){
    val reminderTimeString: String?
        get() {
            return reminderTime?.let { time ->
                PersianCalendar().apply {
                    timeInMillis = time
                }.run {
                    "${"%02d".format(hourOfDay)}:${"%02d".format(minute)}"
                }
            }
        }
    val shouldNotify: Boolean get() = reminderTime != null && isDone.not()

    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("task_id", id)
        }
        return PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun schedule(context: Context) {
        reminderTime?.let { time ->
            if (time < System.currentTimeMillis()) {
                println("task $id won't schedule because reminder time is in past")
            } else {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setExactAndAllowWhileIdle(
                    RTC_WAKEUP,
                    time,
                    createPendingIntent(context)
                )
                println("task $id scheduled at: $reminderTime now: ${System.currentTimeMillis()}, will trigger after: ${time - System.currentTimeMillis()}")
            }

        }
    }

    fun cancelSchedule(context: Context) {
        context.alarmManager.cancel(createPendingIntent(context))
        println("schedule for task $id cancelled")
    }

    fun scheduleOrCancel(context: Context) {
        if (shouldNotify) {
            schedule(context)
        } else {
            cancelSchedule(context)
        }
    }
}