package com.example.kknkt.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtils() {
    companion object{
        var sdf = SimpleDateFormat("yyyy-M-d")

        val TAG :String = "DateUtils"
        public fun addDate(date: Date, howLong: Int): Date{
            var calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE,howLong)
            val dateFormat = "yyyy-MM-dd"
            var addedDate = calendar.time
            return addedDate
        }
        public fun dateInterval(date1: Date, date2: Date): Int{
            val calendar = Calendar.getInstance()
//            calendar.time = date1
            val diff = date2.time - date1.time
            val daysDifferent = (diff / (24 * 60 * 60 * 1000)) as Int
            return  daysDifferent
        }

        public fun dateCompare(string1: String, string2: String): Int{
            if(string1 != "" && string2 != ""){
                var date1 = sdf.parse(string1)
                var date2 = sdf.parse(string2)

                return date1.compareTo(date2)
            }else{
                return 0
            }
        }

        public fun getTodayDate(): String{
            var currentDate = sdf.format(Date())
            Log.d(TAG,currentDate)
            return currentDate
        }
    }
}