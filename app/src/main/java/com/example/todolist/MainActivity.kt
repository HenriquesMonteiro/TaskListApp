package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.todolist.screen.AddTaskScreen
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.viewmodel.TaskListViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TaskListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                    AddTaskScreen(state = viewModel.state.collectAsState().value , onEvent =viewModel::onEvent )
                }
            }
        }

    }
