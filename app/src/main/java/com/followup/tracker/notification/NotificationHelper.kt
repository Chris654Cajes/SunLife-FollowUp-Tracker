package com.followup.tracker.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.followup.tracker.R
import com.followup.tracker.receiver.AlarmReceiver
import com.followup.tracker.ui.main.MainActivity

object NotificationHelper {

    const val CHANNEL_ID = "followup_channel"
    const val CHANNEL_NAME = "Follow-Up Reminders"
    const val EXTRA_FOLLOW_UP_ID = "follow_up_id"
    const val EXTRA_PROSPECT_NAME = "prospect_name"
    const val EXTRA_DAY_OFFSET = "day_offset"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminders to follow up with your prospects"
            enableVibration(true)
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun scheduleFollowUpAlarm(
        context: Context,
        followUpId: Long,
        prospectName: String,
        dayOffset: Int,
        triggerAtMillis: Long
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_FOLLOW_UP_ID, followUpId)
            putExtra(EXTRA_PROSPECT_NAME, prospectName)
            putExtra(EXTRA_DAY_OFFSET, dayOffset)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            followUpId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    fun cancelFollowUpAlarm(context: Context, followUpId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            followUpId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun showFollowUpNotification(
        context: Context,
        followUpId: Long,
        prospectName: String,
        dayOffset: Int
    ) {
        val dayLabel = when (dayOffset) {
            1 -> "Day 1"
            3 -> "Day 3"
            7 -> "Day 7"
            else -> "Day $dayOffset"
        }

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val tapPending = PendingIntent.getActivity(
            context,
            followUpId.toInt(),
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("⏰ Follow-Up Reminder — $dayLabel")
            .setContentText("Time to follow up with $prospectName!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Time to follow up with $prospectName! Open the app to send a pre-written message.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(tapPending)
            .setVibrate(longArrayOf(0, 300, 200, 300))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(followUpId.toInt(), notification)
    }
}
