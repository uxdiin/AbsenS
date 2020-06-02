package com.example.kknkt.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kknkt.repository.RTRepository

class AlarmViewModelFactory(
    val RTRepository: RTRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(RTRepository) as T
    }
}