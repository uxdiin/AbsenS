package com.example.kknkt.repository

import android.os.AsyncTask
import com.example.kknkt.db.RTDatabase
import com.example.kknkt.models.*
import java.io.Serializable

class RTRepository(
    private val db: RTDatabase
)   {
        suspend fun  upsertPerson(person: Person) = db.rtdao().upsert(person)
        fun getAllPerson() = db.rtdao().getAllPerson()
        fun getFreePerson(todayDate: String) = db.rtdao().getFreePerson(todayDate)
        suspend fun deletePerson(person: Person){
            db.rtdao().deletePerson(person)
            db.rtdao().deletePersonAbsenByPersonId(person.id!!)
        }
        fun getAlarm() = db.rtdao().getAlarm()
        suspend fun upsertAlarm(alarm: Alarm) = db.rtdao().upsert(alarm)
        fun getPersonAbsenData(absenIdKey: Int) = db.rtdao().getPersonAbsenData(absenIdKey)
        fun getPersonNotLive(todayDate: String): AsyncTask<String, Void, List<Person>> = LoadFreePersonNotLive().execute(todayDate)
        fun getAllAbsen() = db.rtdao().getAllAbsen()
        suspend fun addAbsen(absen: Absen) = db.rtdao().insertAbsen(absen)
        suspend fun addPersonAbsen(personAbsen: PersonAbsen) = db.rtdao().insertPersonAbsen(personAbsen)
        fun searchPerson(keyName: String) = db.rtdao().searchPerson("%$keyName%")
        suspend fun getPersonByUniqueCode(uniqueCodeKey: String) = db.rtdao().getPersonByUniqueCode(uniqueCodeKey)
        suspend fun getPersonAbsenDataNotLive(getPersonAbsenDataNotliveCallBack: GetPersonAbsenDataNotliveCallBack) {
            getPersonAbsenDataNotliveCallBack.onSucces(db.rtdao().getPersonAbsenDataNotLive())
        }
        inner class LoadFreePersonNotLive: AsyncTask<String, Void, List<Person>>() {

            override fun onPostExecute(result: List<Person>?) {
                super.onPostExecute(result)
            }

            override fun doInBackground(vararg params: String?): List<Person> {
                return db.rtdao().getFreePersonNotLive(params[0]!!)
            }

        }

        public interface GetPersonAbsenDataNotliveCallBack{
            fun onSucces(personAbsenDataExport: List<personAsbenDataExport>)
        }

    }