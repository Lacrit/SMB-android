import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.R
import java.util.*


class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private val CHANNEL_NAME = "High priority channel"
    private val CHANNEL_ID = "com.example.notifications$CHANNEL_NAME"

    fun sendHighPriorityNotification(title: String, body: String, activityName: Class<*>?)
    {
        val intent = Intent(this, activityName)
        val pendingIntent = PendingIntent.getActivity(this,
            267,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(Random().nextInt(), notification)

    }
}