package com.followup.tracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.followup.tracker.data.entity.FollowUp

@Dao
interface FollowUpDao {

    @Query("SELECT * FROM follow_ups WHERE prospectId = :prospectId ORDER BY scheduledAt ASC")
    fun getFollowUpsForProspect(prospectId: Long): LiveData<List<FollowUp>>

    @Query("SELECT * FROM follow_ups WHERE isDone = 0 ORDER BY scheduledAt ASC")
    fun getPendingFollowUps(): LiveData<List<FollowUp>>

    @Query("SELECT * FROM follow_ups WHERE id = :id")
    suspend fun getFollowUpById(id: Long): FollowUp?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(followUp: FollowUp): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(followUps: List<FollowUp>)

    @Update
    suspend fun update(followUp: FollowUp)

    @Query("UPDATE follow_ups SET isDone = 1 WHERE id = :id")
    suspend fun markDone(id: Long)

    @Query("DELETE FROM follow_ups WHERE prospectId = :prospectId")
    suspend fun deleteForProspect(prospectId: Long)
}
