package com.entin.worldnews.presentation.util

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color.RED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.entin.worldnews.R
import com.entin.worldnews.presentation.activity.MainActivity

class NotificationWorkManager(private val appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification = "World News"
        val subtitleNotification = "Read truth - think critical!"

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.icon_notification)
            .setContentIntent(pendingIntent)
            .setContentTitle(titleNotification)
            .setContentText(subtitleNotification)
            .setStyle(NotificationCompat.BigTextStyle().bigText(subtitleNotification))
            .setDefaults(DEFAULT_ALL)
            .setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notification.build())
    }

    companion object {
        const val NOTIFICATION_NAME = "World News"
        const val NOTIFICATION_CHANNEL = "world_channel_01"
    }
}