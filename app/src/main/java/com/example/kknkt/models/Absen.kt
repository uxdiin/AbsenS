package com.example.kknkt.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "absens"
)
data class Absen(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var eventName: String? = null,
    var date: String? = null
):Serializable