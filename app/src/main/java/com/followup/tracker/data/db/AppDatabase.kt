package com.followup.tracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.followup.tracker.data.dao.FollowUpDao
import com.followup.tracker.data.dao.PrewrittenMessageDao
import com.followup.tracker.data.dao.ProspectDao
import com.followup.tracker.data.entity.FollowUp
import com.followup.tracker.data.entity.PrewrittenMessage
import com.followup.tracker.data.entity.Prospect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Prospect::class, FollowUp::class, PrewrittenMessage::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun prospectDao(): ProspectDao
    abstract fun followUpDao(): FollowUpDao
    abstract fun prewrittenMessageDao(): PrewrittenMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "followup_tracker.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    seedPrewrittenMessages(database.prewrittenMessageDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedPrewrittenMessages(dao: PrewrittenMessageDao) {
            if (dao.count() > 0) return
            val messages = listOf(
                // Day 1
                PrewrittenMessage(
                    title = "Initial Check-In",
                    body = "Hi {name}! Just checking in after our conversation. I'd love to answer any questions you might have. Let's chat! 😊",
                    dayOffset = 1
                ),
                PrewrittenMessage(
                    title = "Value Reminder",
                    body = "Hey {name}, I wanted to share a quick reminder of the value we discussed. This could really make a difference for you. Ready to take the next step?",
                    dayOffset = 1
                ),
                PrewrittenMessage(
                    title = "Friendly Follow-Up",
                    body = "Hi {name}! Hope you're doing well. I'm following up from yesterday — I'm here whenever you're ready to move forward! 🚀",
                    dayOffset = 1
                ),
                // Day 3
                PrewrittenMessage(
                    title = "3-Day Check-In",
                    body = "Hey {name}! It's been a couple of days. Just wanted to see if you had any thoughts or questions. I'm happy to hop on a quick call if that helps!",
                    dayOffset = 3
                ),
                PrewrittenMessage(
                    title = "Success Story Share",
                    body = "Hi {name}! I wanted to share a quick success story from someone in a similar situation to yours — the results were amazing. Would you like to hear more?",
                    dayOffset = 3
                ),
                PrewrittenMessage(
                    title = "Addressing Concerns",
                    body = "Hello {name}! I understand decisions like this take time. Is there any concern I can help address? I'm here to make this as easy as possible for you. 💪",
                    dayOffset = 3
                ),
                // Day 7
                PrewrittenMessage(
                    title = "Week Follow-Up",
                    body = "Hi {name}! A week has passed since we connected. I don't want you to miss out on this opportunity — shall we find a time to reconnect this week?",
                    dayOffset = 7
                ),
                PrewrittenMessage(
                    title = "Last Chance Nudge",
                    body = "Hey {name}! I know life gets busy. I'm still here for you whenever you're ready. This opportunity won't be available forever — let me know how I can help! 🌟",
                    dayOffset = 7
                ),
                PrewrittenMessage(
                    title = "Final Check-In",
                    body = "Hello {name}! I respect your time and I'll make this my last follow-up for now. If you ever want to revisit this, I'm just a message away. Wishing you all the best!",
                    dayOffset = 7
                )
            )
            dao.insertAll(messages)
        }
    }
}
