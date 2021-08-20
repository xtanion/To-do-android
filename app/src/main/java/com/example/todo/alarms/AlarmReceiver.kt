package com.example.todo.alarms

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.data.TodoEntity
import java.util.*

class AlarmReceiver():BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Alarm","Receiver ${Date().toString()}")
        //Notification
        val i = Intent(context, MainActivity::class.java)
        val id = context!!.getString(R.string.CHANNEL_ONE)
        val notificationTitle: String? = intent?.getStringExtra("title")

        val title = notificationTitle ?: "Click to open"

        Log.d("Alarm Entity Received",notificationTitle.toString())
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent:PendingIntent = PendingIntent.getActivity(context, 0,i,0)

        val notificationManager = ContextCompat.getSystemService(context,NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(context,pendingIntent,title)

    }
}