package com.followup.tracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prospects")
data class Prospect(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val contact: String,
    val status: String, // "Hot", "Warm", "Cold"
    val createdAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)
