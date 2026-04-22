package com.followup.tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.followup.tracker.FollowUpApp
import com.followup.tracker.data.entity.PrewrittenMessage
import com.followup.tracker.data.repository.FollowUpRepository
import kotlinx.coroutines.launch

class MessagesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FollowUpRepository = (application as FollowUpApp).repository

    val allMessages: LiveData<List<PrewrittenMessage>> = repository.allMessages

    fun insertMessage(message: PrewrittenMessage) {
        viewModelScope.launch { repository.insertMessage(message) }
    }

    fun updateMessage(message: PrewrittenMessage) {
        viewModelScope.launch { repository.updateMessage(message) }
    }

    fun deleteMessage(message: PrewrittenMessage) {
        viewModelScope.launch { repository.deleteMessage(message) }
    }
}
