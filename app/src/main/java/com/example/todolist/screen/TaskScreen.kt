package com.example.todolist.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.events.SortType
import com.example.todolist.events.TaskEvent
import com.example.todolist.state.TaskState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddTaskScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(value = state.title,
                    onValueChange = {
                        onEvent(TaskEvent.SetTitle(it))
                    },
                    placeholder = {
                        Text(text = "Add Title")
                    }
                )
                TextField(value = state.task, onValueChange = {
                    onEvent(TaskEvent.SetTask(it))
                },
                    placeholder = {
                        Text(text = "Add Task")
                    }
                )
                Divider(Modifier.height(1.dp))
                Button(
                    onClick = {
                        onEvent(TaskEvent.SaveTask)
                    }
                ) {
                    Text("Save")
                }
            }
        }) {
        Scaffold(
            floatingActionButton={
                FloatingActionButton(onClick = {
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                }) {
                    Icon(   imageVector = Icons.Default.Add,
                        contentDescription ="Add Contact" )
                }
            }
        ) {padding->
            Column(

                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)){
                    MainScreen(state = state, onEvent = onEvent)
                }


        }
    }
}

@Composable
fun MainScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit
) {
    Scaffold { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(TaskEvent.SortTask(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = state.sortType == sortType, onClick = {
                                onEvent(TaskEvent.SortTask(sortType))
                            }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(state.tasks) { task ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        androidx.compose.material.Text(
                            text = task.Title,
                            fontSize = 20.sp
                        )
                        Text(text = task.Task, fontSize = 15.sp)
                    }
                    IconButton(onClick = {
                        onEvent(TaskEvent.DeleteTask(task))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Task"
                        )
                    }
                }
            }
        }
    }
}