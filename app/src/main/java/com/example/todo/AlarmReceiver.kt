package com.example.todo

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Alarm","Receiver ${Date().toString()}")
        //Notification
        val i = Intent(context,MainActivity::class.java)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent:PendingIntent = PendingIntent.getActivity(context,0,i,0)

        val builder = NotificationCompat.Builder(context!!,"OpenTodo")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test Notification")
            .setContentText("Testing the alarm manager")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())

    }
}