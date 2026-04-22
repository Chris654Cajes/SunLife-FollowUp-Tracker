package com.followup.tracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.followup.tracker.FollowUpApp
import com.followup.tracker.data.entity.FollowUp
import com.followup.tracker.data.entity.Prospect
import com.followup.tracker.data.repository.FollowUpRepository
import com.followup.tracker.notification.NotificationHelper
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FollowUpRepository = (application as FollowUpApp).repository

    val allProspects: LiveData<List<Prospect>> = repository.allProspects
    val pendingFollowUps: LiveData<List<FollowUp>> = repository.pendingFollowUps

    fun addProspect(name: String, contact: String, status: String, context: Context) {
        viewModelScope.launch {
            val prospect = Prospect(name = name, contact = contact, status = status)
            val prospectId = repository.insertProspect(prospect)
            scheduleFollowUps(context, prospectId, name)
        }
    }

    private suspend fun scheduleFollowUps(context: Context, prospectId: Long, name: String) {
        val now = System.currentTimeMillis()
        val dayOffsets = listOf(1, 3, 7)
        val followUps = dayOffsets.map { days ->
            FollowUp(
                prospectId = prospectId,
                scheduledAt = now + TimeUnit.DAYS.toMillis(days.toLong()),
                dayOffset = days
            )
        }
        repository.insertFollowUps(followUps)

        // Re-fetch to get generated IDs
        val saved = repository.getFollowUpsForProspect(prospectId).value
        saved?.forEach { fu ->
            NotificationHelper.scheduleFollowUpAlarm(
                context,
                fu.id,
                name,
                fu.dayOffset,
                fu.scheduledAt
            )
        }

        // Schedule via ID range fallback (IDs not immediately available from LiveData)
        // We query directly for scheduling
        scheduleAlarmsForNewFollowUps(context, prospectId, name, followUps, now)
    }

    private suspend fun scheduleAlarmsForNewFollowUps(
        context: Context,
        prospectId: Long,
        name: String,
        followUps: List<FollowUp>,
        now: Long
    ) {
        // Fetch saved follow-ups directly from DB
        val db = (context.applicationContext as FollowUpApp).database
        val dayOffsets = listOf(1, 3, 7)
        dayOffsets.forEach { days ->
            val triggerAt = now + TimeUnit.DAYS.toMillis(days.toLong())
            // Find follow-up from DB by prospectId and dayOffset
            val allFu = db.followUpDao().getFollowUpsForProspect(prospectId).value
            val fu = allFu?.firstOrNull { it.dayOffset == days }
            if (fu != null) {
                NotificationHelper.scheduleFollowUpAlarm(context, fu.id, name, days, triggerAt)
            }
        }
    }

    fun scheduleAlarmsAfterInsert(context: Context, prospectId: Long, prospectName: String) {
        viewModelScope.launch {
            val db = (context.applicationContext as FollowUpApp).database
            val now = System.currentTimeMillis()
            val dayOffsets = listOf(1, 3, 7)
            dayOffsets.forEach { days ->
                val triggerAt = now + TimeUnit.DAYS.toMillis(days.toLong())
                val followUp = FollowUp(
                    prospectId = prospectId,
                    scheduledAt = triggerAt,
                    dayOffset = days
                )
                val fuId = db.followUpDao().insert(followUp)
                NotificationHelper.scheduleFollowUpAlarm(context, fuId, prospectName, days, triggerAt)
            }
        }
    }

    fun deleteProspect(prospect: Prospect, context: Context) {
        viewModelScope.launch {
            // Cancel alarms first
            val db = (context.applicationContext as FollowUpApp).database
            val followUps = db.followUpDao().getFollowUpsForProspect(prospect.id).value
            followUps?.forEach { NotificationHelper.cancelFollowUpAlarm(context, it.id) }
            repository.deleteProspect(prospect)
        }
    }

    fun markFollowUpDone(id: Long) {
        viewModelScope.launch {
            repository.markFollowUpDone(id)
        }
    }
}
