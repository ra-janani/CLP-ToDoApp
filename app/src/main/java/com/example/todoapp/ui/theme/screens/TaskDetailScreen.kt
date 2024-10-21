package com.example.todoapp.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todoapp.data.model.SubTask
import com.example.todoapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Int,
    navController: NavHostController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    // Retrieve the task from the ViewModel based on the taskId
    val task = viewModel.getTaskById(taskId).collectAsState(initial = null).value
    val subTasks = viewModel.getSubTasksForTask(taskId).collectAsState(initial = emptyList()).value

    var newSubTaskTitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            task?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )

                // List of subtasks
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f) // Allow LazyColumn to take remaining space
                ) {
                    items(subTasks) { subTask ->
                        SubTaskItem(
                            subTask = subTask,
                            onToggleComplete = { updatedSubTask ->
                                viewModel.updateSubTask(updatedSubTask)
                            },
                            onDelete = {
                                viewModel.deleteSubTask(it)
                            }
                        )
                    }
                }

                // Add new subtask UI
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    TextField(
                        value = newSubTaskTitle,
                        onValueChange = { newSubTaskTitle = it },
                        label = { Text("New SubTask") },
                        modifier = Modifier.weight(1f) // Make TextField take remaining space
                    )
                    Button(onClick = {
                        if (newSubTaskTitle.isNotEmpty()) {
                            viewModel.addSubTask(
                                taskId,
                                newSubTaskTitle
                            )
                            newSubTaskTitle = "" // Clear input
                        }
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}


@Composable
fun SubTaskItem(
    subTask: SubTask,
    onToggleComplete: (SubTask) -> Unit,
    onDelete: (SubTask) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = subTask.isCompleted,
                onCheckedChange = {
                    onToggleComplete(subTask.copy(isCompleted = it))
                }
            )
            Text(
                text = subTask.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null,
                color = if (subTask.isCompleted) Color.Gray else Color.Black
            )
            IconButton(onClick = { onDelete(subTask) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete SubTask"
                )
            }
        }
    }
}
