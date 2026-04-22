package com.followup.tracker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.followup.tracker.notification.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val followUpId = intent.getLongExtra(NotificationHelper.EXTRA_FOLLOW_UP_ID, -1L)
        val prospectName = intent.getStringExtra(NotificationHelper.EXTRA_PROSPECT_NAME) ?: "your prospect"
        val dayOffset = intent.getIntExtra(NotificationHelper.EXTRA_DAY_OFFSET, 1)

        if (followUpId != -1L) {
            NotificationHelper.showFollowUpNotification(context, followUpId, prospectName, dayOffset)
        }
    }
}
