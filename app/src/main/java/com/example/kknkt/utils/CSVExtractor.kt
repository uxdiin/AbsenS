package com.example.kknkt.utils

import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class CSVExtractor {
    companion object{
        fun extract(text: String): List<String>{
            val pattern = Pattern.compile("(?:(?<=\")([^\"]*)(?=\"))|(?<=,|^)([^,]*)(?=,|\$)")
            val result = pattern.matcher(text)

            var list: ArrayList<String> = ArrayList<String>()
            while (result.find()){
                list.add(text.substring(result.start(),result.end()))
            }

            return list
        }
    }
}