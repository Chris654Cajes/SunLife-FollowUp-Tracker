package com.followup.tracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.followup.tracker.data.entity.PrewrittenMessage

@Dao
interface PrewrittenMessageDao {

    @Query("SELECT * FROM prewritten_messages ORDER BY dayOffset ASC, id ASC")
    fun getAllMessages(): LiveData<List<PrewrittenMessage>>

    @Query("SELECT * FROM prewritten_messages WHERE dayOffset = :dayOffset")
    suspend fun getMessagesByDay(dayOffset: Int): List<PrewrittenMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: PrewrittenMessage): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<PrewrittenMessage>)

    @Update
    suspend fun update(message: PrewrittenMessage)

    @Delete
    suspend fun delete(message: PrewrittenMessage)

    @Query("SELECT COUNT(*) FROM prewritten_messages")
    suspend fun count(): Int
}
