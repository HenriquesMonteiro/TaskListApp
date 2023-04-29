package com.example.todolist.events

import com.example.todolist.data.DataList

sealed interface TaskEvent {

        object SaveTask: TaskEvent
        data class SetTitle(val title: String): TaskEvent
        data class SetTask(val task: String): TaskEvent
        object ShowDialog: TaskEvent
        object HideDialog:TaskEvent
        data class SortTask(val sortType: SortType): TaskEvent
        data class DeleteTask(val task: DataList): TaskEvent

    }

