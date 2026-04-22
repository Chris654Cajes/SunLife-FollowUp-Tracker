package com.followup.tracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prewritten_messages")
data class PrewrittenMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val body: String,
    val dayOffset: Int  // 1, 3, or 7 — which follow-up day this fits
)
