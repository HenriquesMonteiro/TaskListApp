package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DataList::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao: TaskDao


}
