package ir.unalzo

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import ir.unalzo.database.dao.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderReceiver : BroadcastReceiver(), KoinComponent {
    private val taskDao: TaskDao by inject()
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override fun onReceive(context: Context?, intent: Intent?) {
        println("schedule triggered")
        scope.launch(Dispatchers.IO) {
            val taskId = intent?.getIntExtra("task_id", 0) ?: return@launch
            val task = taskDao.getTaskById(taskId) ?: return@launch

            withContext(Dispatchers.Main) {
                if (context != null) {
                    val intent = Intent(context, MainActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        1,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val notification = Notification.Builder(context, "channel")
                        .setContentTitle("یادآور")
                        .setContentText(task.title)
                        .setSmallIcon(R.drawable.app_logo)
                        .setContentIntent(pendingIntent)
                        .build()
                    NotificationManagerCompat.from(context).notify(task.id, notification)
                }
            }
        }
    }
}