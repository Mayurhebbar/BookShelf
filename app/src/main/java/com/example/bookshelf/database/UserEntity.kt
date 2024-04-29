package com.example.bookshelf.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = dbUserTable)
data class Users(
    @PrimaryKey (autoGenerate = true)
    val id:Long=0,
    var password: String?="",
    var name:String?="",
    var email:String?="",
    var country:String?="",
    var booksList: MutableList<Book>? = null
)

data class Book(var bookTitle : String? = "",
                var annotations : List<String>? = null,
                var isBookMarked : Boolean? = false)