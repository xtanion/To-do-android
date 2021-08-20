package com.example.todo.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmService(private val context: Context) {
    private val alarmManager:AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    private lateinit var pendingIntent:PendingIntent

    fun setExactAlarm(timeInMillis: Long,requestCode: Int,intent: Intent){
        //TODO
        pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        setAlarm(timeInMillis,pendingIntent)
    }

    fun setRepetitiveAlarm(){
        //Every Day
    }

    private fun setAlarm(timeInMillis:Long,pendingIntent: PendingIntent){
        alarmManager?.let {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent)
            }else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent)
            }
        }
    }

}