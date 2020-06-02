package com.example.kknkt.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kknkt.models.*

@Dao
interface RTDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(person: Person): Long

    @Query("SELECT * FROM persons where freeDate like :todayDate")
    fun getFreePerson(todayDate: String): LiveData<List<Person>>

    @Query("SELECT * FROM persons where freeDate like :todayDate")
    fun getFreePersonNotLive(todayDate: String): List<Person>

    @Query("SELECT * FROM persons WHERE uniqueCode = :uniqueCodeKey")
    suspend fun getPersonByUniqueCode(uniqueCodeKey: String): List<Person>

    @Query("SELECT * FROM persons ")
    fun getAllPerson(): LiveData<List<Person>>

    @Delete
    suspend fun deletePerson(person: Person)

    @Query("SELECT * FROM alarms")
    fun getAlarm():LiveData<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(alarm: Alarm): Long

    @Query("SELECT * FROM absens")
    fun getAllAbsen():LiveData<List<Absen>>

    @Insert()
    suspend fun insertAbsen(absen: Absen): Long

    @Query("SELECT persons.nik,persons.name,persons.ttl,gender,address,rt,rw,village,subDistrict,religion,marriageStatus,job,citizenship,personId,absenId,temperature FROM person_absen JOIN persons ON persons.id = person_absen.personId join absens on person_absen.absenId = absens.id where absenId = :absenIdKey")
    fun getPersonAbsenData(absenIdKey: Int):LiveData<List<PersonAbsenData>>

    @Insert()
    suspend fun insertPersonAbsen(personAbsen: PersonAbsen): Long

    @Query("SELECT * from persons where name like :nameKey")
    fun searchPerson(nameKey: String):LiveData<List<Person>>
}
