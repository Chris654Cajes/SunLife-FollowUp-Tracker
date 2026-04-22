package com.followup.tracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "follow_ups",
    foreignKeys = [ForeignKey(
        entity = Prospect::class,
        parentColumns = ["id"],
        childColumns = ["prospectId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("prospectId")]
)
data class FollowUp(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val prospectId: Long,
    val scheduledAt: Long,   // timestamp in millis
    val dayOffset: Int,      // 1, 3, or 7
    val isDone: Boolean = false,
    val message: String = ""
)
