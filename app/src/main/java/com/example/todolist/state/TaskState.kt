package com.example.todolist.state

import com.example.todolist.data.DataList
import com.example.todolist.events.SortType

data class TaskState(
    val tasks: List<DataList> = emptyList(),
    val title: String= "",
    val task: String= "",
    val isAddingList: Boolean=false,
    val sortType: SortType =SortType.Title

)
