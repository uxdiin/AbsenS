package com.example.kknkt.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.kknkt.models.Absen
import com.example.kknkt.models.Alarm
import com.example.kknkt.models.Person
import com.example.kknkt.models.PersonAbsen

@Database(
    entities =  [Person::class,Alarm::class,Absen::class,PersonAbsen::class],
    version = 1
)
abstract class RTDatabase : RoomDatabase(){
    abstract fun rtdao(): RTDAO

    companion object {
        @Volatile
        private var instance: RTDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            databaseBuilder(
                context.applicationContext,
                RTDatabase::class.java,
                "rt_db.db"
            ).build()
    }
}