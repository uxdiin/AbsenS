package com.example.kknkt.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "alarms"
)
data class Alarm(
    @PrimaryKey(autoGenerate = false)
    var id: Int? = 1,
    var time: String? = null,
    var day: Int? = null,
    var isOn: Boolean? = false
)