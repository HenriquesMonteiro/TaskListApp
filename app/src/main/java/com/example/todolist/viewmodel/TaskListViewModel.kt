package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.DataList
import com.example.todolist.events.SortType
import com.example.todolist.events.TaskEvent
import com.example.todolist.state.TaskState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val app: Application
) : AndroidViewModel(app) {
    private val db by lazy {
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "DataList.db"
        ).build()
    }
    private val dao = db.dao


    private val _sortType = MutableStateFlow(SortType.Title)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _tasks = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.Title -> dao.getListOrderedById()

            SortType.Task -> dao.getContactOrderedByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TaskState())

    val state = combine(_state, _sortType, _tasks) { state, sortType, tasks ->
        state.copy(
            tasks = tasks,
            sortType = sortType
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    dao.deleteList(event.task)
                }
            }

            TaskEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingList = false
                    )
                }
            }

            TaskEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingList = true
                    )

                }
            }

            is TaskEvent.SortTask -> {
                _sortType.value = event.sortType
            }

            TaskEvent.SaveTask -> {
                val task = state.value.task
                val title = state.value.title
                if (task.isBlank() || title.isBlank()) {
                    return
                }
                val tasks = DataList(
                    Task = task,
                    Title = title
                )
                viewModelScope.launch {
                    dao.upsertList(tasks)
                }
                _state.update {
                    it.copy(
                        isAddingList = false,
                        task = "",
                        title = "",
                    )
                }
            }

            is TaskEvent.SetTask -> {
                _state.update {
                    it.copy(
                        task = event.task
                    )
                }
            }

            is TaskEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )

                }
            }
        }
    }


}