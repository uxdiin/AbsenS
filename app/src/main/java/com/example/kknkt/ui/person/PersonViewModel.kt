package com.example.kknkt.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kknkt.models.Absen
import com.example.kknkt.repository.RTRepository
import kotlinx.coroutines.launch
import com.example.kknkt.models.Person
import com.example.kknkt.models.PersonAbsen
import com.example.kknkt.utils.UniqueCodeGenerator

class PersonViewModel(
    private val RTRepository: RTRepository
) : ViewModel() {

    fun savePerson(person: Person) = viewModelScope.launch {
        RTRepository.upsertPerson(person)
    }

    fun getFreePerson(todayDate: String) = RTRepository.getFreePerson(todayDate)

    fun getAllPerson() = RTRepository.getAllPerson()

    fun deletePerson(person: Person) = viewModelScope.launch {
        RTRepository.deletePerson(person)
    }

    fun getAllAbsen() = RTRepository.getAllAbsen()

    fun getPersonAbsenData(absenIdKey: Int) = RTRepository.getPersonAbsenData(absenIdKey)

    fun addAbsen(absen: Absen) = viewModelScope.launch {
        RTRepository.addAbsen(absen)
    }

    fun addPersonAbsen(personAbsen: PersonAbsen) = viewModelScope.launch {
        RTRepository.addPersonAbsen(personAbsen)
    }

    fun searchPerson(keyName: String) = RTRepository.searchPerson(keyName)

    fun getPersonByUniqueCode(uniqueCodeKey: String, selectOnePersonCallBack: SelectOnePersonCallBack) = viewModelScope.launch {
        var listPerson = RTRepository.getPersonByUniqueCode(uniqueCodeKey)
        selectOnePersonCallBack.onSucces(listPerson)
    }

    public interface SelectOnePersonCallBack{
        fun onSucces(listPerson: List<Person>)
    }
}