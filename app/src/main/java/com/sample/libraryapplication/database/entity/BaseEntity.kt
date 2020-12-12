package com.sample.libraryapplication.database.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.Gson

abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    constructor()
    override fun toString(): String {
        return Gson().toJson(this)
    }
}