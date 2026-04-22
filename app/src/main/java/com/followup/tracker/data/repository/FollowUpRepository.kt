package com.followup.tracker.data.repository

import androidx.lifecycle.LiveData
import com.followup.tracker.data.dao.FollowUpDao
import com.followup.tracker.data.dao.PrewrittenMessageDao
import com.followup.tracker.data.dao.ProspectDao
import com.followup.tracker.data.entity.FollowUp
import com.followup.tracker.data.entity.PrewrittenMessage
import com.followup.tracker.data.entity.Prospect

class FollowUpRepository(
    private val prospectDao: ProspectDao,
    private val followUpDao: FollowUpDao,
    private val messageDao: PrewrittenMessageDao
) {

    // Prospects
    val allProspects: LiveData<List<Prospect>> = prospectDao.getAllProspects()

    suspend fun insertProspect(prospect: Prospect): Long = prospectDao.insert(prospect)

    suspend fun updateProspect(prospect: Prospect) = prospectDao.update(prospect)

    suspend fun deleteProspect(prospect: Prospect) = prospectDao.delete(prospect)

    suspend fun getProspectById(id: Long): Prospect? = prospectDao.getProspectById(id)

    // Follow-Ups
    fun getFollowUpsForProspect(prospectId: Long): LiveData<List<FollowUp>> =
        followUpDao.getFollowUpsForProspect(prospectId)

    val pendingFollowUps: LiveData<List<FollowUp>> = followUpDao.getPendingFollowUps()

    suspend fun insertFollowUps(followUps: List<FollowUp>) = followUpDao.insertAll(followUps)

    suspend fun markFollowUpDone(id: Long) = followUpDao.markDone(id)

    suspend fun getFollowUpById(id: Long): FollowUp? = followUpDao.getFollowUpById(id)

    // Messages
    val allMessages: LiveData<List<PrewrittenMessage>> = messageDao.getAllMessages()

    suspend fun getMessagesByDay(dayOffset: Int): List<PrewrittenMessage> =
        messageDao.getMessagesByDay(dayOffset)

    suspend fun insertMessage(message: PrewrittenMessage): Long = messageDao.insert(message)

    suspend fun updateMessage(message: PrewrittenMessage) = messageDao.update(message)

    suspend fun deleteMessage(message: PrewrittenMessage) = messageDao.delete(message)
}
