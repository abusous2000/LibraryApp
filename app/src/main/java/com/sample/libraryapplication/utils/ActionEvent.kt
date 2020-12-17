package com.sample.libraryapplication.utils

data class ActionEvent(var actionEvent: String, var data: String) {
    companion object{
        const val INSERT_BOOK_AE     = "insertBook"
        const val UPDATE_BOOK_AE     = "updateBook"
        const val DELETE_BOOK_AE     = "deleteBook"
        const val INSERT_CATEGORY_AE = "insertCategory"
        const val UPDATE_CATEGORY_AE = "updateCategory"
        const val DELETE_CATEGORY_AE = "deleteCategory"
    }
}