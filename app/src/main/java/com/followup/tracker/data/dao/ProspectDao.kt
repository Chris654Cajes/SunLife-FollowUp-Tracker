package com.followup.tracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.followup.tracker.data.entity.Prospect

@Dao
interface ProspectDao {

    @Query("SELECT * FROM prospects ORDER BY createdAt DESC")
    fun getAllProspects(): LiveData<List<Prospect>>

    @Query("SELECT * FROM prospects WHERE id = :id")
    suspend fun getProspectById(id: Long): Prospect?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prospect: Prospect): Long

    @Update
    suspend fun update(prospect: Prospect)

    @Delete
    suspend fun delete(prospect: Prospect)

    @Query("DELETE FROM prospects WHERE id = :id")
    suspend fun deleteById(id: Long)
}
