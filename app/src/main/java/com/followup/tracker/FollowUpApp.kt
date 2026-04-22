package com.followup.tracker

import android.app.Application
import com.followup.tracker.data.db.AppDatabase
import com.followup.tracker.data.repository.FollowUpRepository
import com.followup.tracker.notification.NotificationHelper

class FollowUpApp : Application() {

    val database by lazy { AppDatabase.getInstance(this) }

    val repository by lazy {
        FollowUpRepository(
            database.prospectDao(),
            database.followUpDao(),
            database.prewrittenMessageDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
