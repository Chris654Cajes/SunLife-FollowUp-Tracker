package com.followup.tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.followup.tracker.FollowUpApp
import com.followup.tracker.data.entity.Prospect
import com.followup.tracker.data.repository.FollowUpRepository
import kotlinx.coroutines.launch

class AddProspectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FollowUpRepository = (application as FollowUpApp).repository

    suspend fun insertProspect(prospect: Prospect): Long {
        return repository.insertProspect(prospect)
    }
}
