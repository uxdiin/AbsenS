package com.example.kknkt.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "persons"
)
data class Person(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var nik: String? = null,
    var name: String? = null,
    var ttl: String? = null,
    var gender: Int? = null,
    var address: String? = null,
    var rt: String? = null,
    var rw: String? = null,
    var village: String? = null,
    var subDistrict: String? = null,
    var religion: String? = null,
    var marriageStatus: String? = null,
    var job: String? = null,
    var citizenship: String? = null,
    var arrivalDate: String? = null,
    var freeDate: String? = null,
    var quarantineStatus: String? = null,
    var uniqueCode: String? = null
): Serializable