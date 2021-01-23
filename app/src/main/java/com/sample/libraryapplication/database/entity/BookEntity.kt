package com.sample.libraryapplication.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Ignore
import com.google.gson.Gson
import com.sample.libraryapplication.R
import java.util.*

@Entity(tableName = "books",
    foreignKeys = [ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["book_category_id"], onDelete = CASCADE)])
class BookEntity(): Parcelable, BaseEntity() {
    @ColumnInfo(name = "book_name")
    var bookName: String? = null
    @ColumnInfo(name = "book_unit_price")
    var bookUnitPrice: Double? = null
    @ColumnInfo(name = "book_category_id",index = true)
    var bookCategoryID: Long? = null
    var resourceId: Int = R.drawable.ic_launcher_background

    @Ignore
    var url: String? = null

    constructor(id: Long?, bookName: String?, bookUnitPrice: Double?, bookCategoryID: Long?, resourceId: Int = R.drawable.ic_launcher_background):this() {
        this.id = id
        this.bookName = bookName
        this.bookUnitPrice = bookUnitPrice
        this.bookCategoryID = bookCategoryID
        this.resourceId = resourceId
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        bookName = parcel.readString()
        bookUnitPrice = parcel.readValue(Double::class.java.classLoader) as? Double
        bookCategoryID = parcel.readValue(Long::class.java.classLoader) as? Long
        resourceId = parcel.readInt()
    }

    // for DiffUtil class
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BookEntity) return false
        val book = other as? BookEntity
        return id == book?.id
                && bookName == book?.bookName
                && bookUnitPrice == book?.bookUnitPrice
                && bookCategoryID == book?.bookCategoryID
                && resourceId == book?.resourceId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, bookName, bookUnitPrice, bookCategoryID)
    }
    // parcelable stuff
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(bookName)
        parcel.writeValue(bookUnitPrice)
        parcel.writeValue(bookCategoryID)
        parcel.writeInt(resourceId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookEntity> {
        override fun createFromParcel(parcel: Parcel): BookEntity {
            return BookEntity(parcel)
        }

        override fun newArray(size: Int): Array<BookEntity?> {
            return arrayOfNulls(size)
        }
    }
}