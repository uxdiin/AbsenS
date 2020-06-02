package com.example.kknkt.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kknkt.models.Alarm
import com.example.kknkt.repository.RTRepository
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val RTRepository: RTRepository): ViewModel(
) {
    public fun saveAlarmTime(alarm: Alarm) = viewModelScope.launch {
        RTRepository.upsertAlarm(alarm)
    }

    public fun getAlarm() = RTRepository.getAlarm()

}