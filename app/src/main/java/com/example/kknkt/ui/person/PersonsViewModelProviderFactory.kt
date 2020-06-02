package com.example.kknkt.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kknkt.repository.RTRepository

class PersonsViewModelProviderFactory(
    val RTRepository: RTRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PersonViewModel(RTRepository) as T
    }
}