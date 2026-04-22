package com.followup.tracker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.followup.tracker.data.db.AppDatabase
import com.followup.tracker.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(context)
            val followUps = db.followUpDao().getPendingFollowUps().value ?: return@launch
            val now = System.currentTimeMillis()

            for (fu in followUps) {
                if (fu.scheduledAt > now) {
                    val prospect = db.prospectDao().getProspectById(fu.prospectId) ?: continue
                    NotificationHelper.scheduleFollowUpAlarm(
                        context,
                        fu.id,
                        prospect.name,
                        fu.dayOffset,
                        fu.scheduledAt
                    )
                }
            }
        }
    }
}
