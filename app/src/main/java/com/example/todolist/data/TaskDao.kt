package com.example.todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {


    @Delete
    fun deleteList(task: DataList)
    @Upsert
    suspend fun upsertList( task: DataList)

    @Query("SELECT * FROM DataList ORDER BY Id ASC")
    fun getListOrderedById(): Flow<List<DataList>>

    @Query("SELECT * FROM DataList ORDER BY Title ASC")
    fun getContactOrderedByTitle(): Flow<List<DataList>>


}
