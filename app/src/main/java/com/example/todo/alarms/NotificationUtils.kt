package com.example.todo.alarms

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.todo.R
import com.example.todo.data.TodoEntity

fun NotificationManager.sendNotification(context: Context,pendingIntent: PendingIntent,title:String){
    val NOTIFICATION_ID = 123
    val id = context.getString(R.string.CHANNEL_ONE)

    val builder = NotificationCompat.Builder(context,id)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Reminder")
        .setContentText(title)
        .setColor(ContextCompat.getColor(context,R.color.deep_blue))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setCategory(NotificationCompat.CATEGORY_ALARM)

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(NOTIFICATION_ID,builder.build())
}