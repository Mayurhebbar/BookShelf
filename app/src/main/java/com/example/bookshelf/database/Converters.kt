package com.example.bookshelf.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromJson(value: String): List<Book>? {
        val listType = object : TypeToken<List<Book>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Book>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun restoreList(listOfString: String): List<String>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<String>>() {

        }.type)
    }

    @TypeConverter
    fun saveList(listOfString: List<String>?): String {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun restoreArrayList(listOfString: String): ArrayList<String>? {
        return Gson().fromJson(listOfString, object : TypeToken<ArrayList<String>>() {

        }.type)
    }

    @TypeConverter
    fun saveArrayList(listOfString: ArrayList<String>?): String {
        return Gson().toJson(listOfString)
    }
}