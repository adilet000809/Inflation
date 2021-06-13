package com.example.diploma.ui.home

import androidx.room.ColumnInfo


data class Supermarket(
    @ColumnInfo(name = "supermarket_id") val id: Int?,
    @ColumnInfo(name = "supermarket_name") val name: String?
)
