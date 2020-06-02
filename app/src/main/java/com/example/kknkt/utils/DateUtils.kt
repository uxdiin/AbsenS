package com.example.kknkt.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtils() {
    companion object{

        val TAG :String = "DateUtils"
        public fun addDate(date: Date, howLong: Int): Date{
            var calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE,howLong)
            val dateFormat = "yyyy-MM-dd"
            var addedDate = calendar.time
            return addedDate
        }
        public fun getTodayDate(): String{
//            val current = LocalDateTime.now()
            val sdf = SimpleDateFormat("yyyy-M-dd")
            var currentDate = sdf.format(Date())
//            currentDate = "'"+currentDate+"'"
            Log.d(TAG,currentDate)
            return currentDate
//            System.out.println(" C DATE is  "+currentDate)
//            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
//            val formattedDate = formatter.format()
        }
    }
}