package com.example.diploma.ui.productInfo

import androidx.room.ColumnInfo

data class Category(
    @ColumnInfo(name = "category_id") val id: Int?,
    @ColumnInfo(name = "category_name") val name: String?
)