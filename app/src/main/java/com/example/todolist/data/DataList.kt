package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "DataList")
data class DataList(

    val Title: String,
    val Task: String,
    @PrimaryKey(autoGenerate = true)
    val Id: Int = 0
)
