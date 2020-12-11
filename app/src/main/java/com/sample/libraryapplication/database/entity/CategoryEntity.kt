package com.sample.libraryapplication.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

//@Entity(tableName = "categories")
//data class CategoryEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    var id: Long,
//    @ColumnInfo(name = "category_name")
//    var categoryName: String,
//    @ColumnInfo(name = "category_description")
//    var categoryDesc: String){
//
//    // for spinner
//    override fun toString(): String {
//        return categoryName
//    }
//}
@Entity(tableName = "categories")
class CategoryEntity : BaseEntity {

    @ColumnInfo(name = "category_name")
    lateinit var categoryName: String
    @ColumnInfo(name = "category_description")
    lateinit var categoryDesc: String

    constructor(id: Long?, categoryName: String, categoryDesc: String):super() {
        this.id =  id
        this.categoryDesc = categoryDesc
        this.categoryName = categoryName
    }
    // for spinner
    override fun toString(): String {
        return categoryName
    }

}