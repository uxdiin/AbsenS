package com.example.kknkt.utils

class UniqueCodeGenerator {
    companion object{
        fun formatToCode(text1: String, text2: String): String{
            val word = text1.count()
            val result = text1 + word.toString() + text2.substring(0,5)
            return result
        }
    }
}