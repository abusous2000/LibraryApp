package com.sample.libraryapplication.database.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

abstract class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    constructor()
}