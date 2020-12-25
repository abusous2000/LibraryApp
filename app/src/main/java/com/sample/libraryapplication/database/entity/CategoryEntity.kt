package com.sample.libraryapplication.database.entity

import android.os.Parcel
import android.os.Parcelable
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
class CategoryEntity() : BaseEntity(), Parcelable {
    @ColumnInfo(name = "category_name")
    var categoryName: String?=null
    @ColumnInfo(name = "category_description")
    var categoryDesc: String?=null

    constructor(parcel: Parcel) : this() {
        categoryName = parcel.readString()!!
        categoryDesc = parcel.readString()!!
    }

    constructor(id: Long?, categoryName: String, categoryDesc: String):this() {
        this.id =  id
        this.categoryDesc = categoryDesc
        this.categoryName = categoryName
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryName)
        parcel.writeString(categoryDesc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryEntity> {
        override fun createFromParcel(parcel: Parcel): CategoryEntity {
            return CategoryEntity(parcel)
        }

        override fun newArray(size: Int): Array<CategoryEntity?> {
            return arrayOfNulls(size)
        }
    }
}