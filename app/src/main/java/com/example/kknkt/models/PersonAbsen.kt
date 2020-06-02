package com.example.kknkt.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "person_absen"
//    foreignKeys = arrayOf(
//        ForeignKey(
//            entity = Person::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("personId"),
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = Absen::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("absenId"),
//            onDelete = ForeignKey.CASCADE
//        )
//    )
)
data class PersonAbsen(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var personId: Int? = null,
    var absenId: Int? = null,
    var temperature: Int? = null
)