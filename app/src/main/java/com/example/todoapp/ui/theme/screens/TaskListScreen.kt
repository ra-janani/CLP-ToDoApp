package com.example.todoapp.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todoapp.R
import com.example.todoapp.data.model.Task
import com.example.todoapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navController: NavHostController,
    viewModel: TaskViewModel = hiltViewModel() // Inject your ViewModel here
) {
    val tasks = viewModel.allTasks.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    if (showDialog && taskToDelete != null) {
        AlertDialog(onDismissRequest = { showDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(onClick = {
                    taskToDelete?.let {
                        viewModel.deleteTask(it)
                    }
                    showDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            })
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Task List") })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate("add_task") // Navigate to the AddTaskScreen
        }) {
            Text("+")
        }
    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(tasks.value) { task ->
                TaskItem(task, onDelete = {
                    // Instead of deleting immediately, show the dialog first
                    taskToDelete = it  // Set the task to be deleted
                    showDialog = true   // Show the delete confirmation dialog
                }, onToggleComplete = { updatedTask ->
                    viewModel.updateTask(updatedTask) // Update the task's completion status
                }, onTaskClick = { // Navigate to the TaskDetailScreen with the taskId
                    navController.navigate("task_detail/${task.id}")
                })
            }
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onDelete: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onTaskClick: (Task) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick(task) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) { // Use Box to position delete button
            // Content of the card
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = task.isCompleted, onCheckedChange = {
                        onToggleComplete(task.copy(isCompleted = it)) // Pass updated task to the handler
                    })
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        color = if (task.isCompleted) Color.Gray else Color.Black,
                        modifier = Modifier.weight(1f) // Makes the title take the remaining space
                    )
                }

                // Description should start from the same point as title
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted) Color.Gray else Color.Black,
                    modifier = Modifier.padding(start = 50.dp) // Aligns description with title
                )
            }

            // Align delete button to the bottom right corner
            IconButton(
                onClick = { onDelete(task) },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Position it at the bottom end (right)
                    .padding(top = 8.dp) // Optional padding from bottom
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete Task",
                    tint = Color.Unspecified // This ensures the icon keeps its original color
                )
            }
        }
    }
}


//fun TaskItem(task: Task, onDelete: (Task) -> Unit, onToggleComplete: (Task) -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Checkbox(
//                    checked = task.isCompleted,
//                    onCheckedChange = {
//                        onToggleComplete(task.copy(isCompleted = it)) // Pass updated task to the handler
//                    }
//                )
//                Text(
//                    text = task.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
//                    color = if (task.isCompleted) Color.Gray else Color.Black,
//                    modifier = Modifier.weight(1f) // Makes the title take the remaining space
//
//                )
//            }
//                Text(
//                    text = task.description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
//                    color = if (task.isCompleted) Color.Gray else Color.Black,
//                    modifier = Modifier.padding(start = 50.dp) // Aligns description with title
//
//                )
//
//            // Add delete button/icon here
//            IconButton(onClick = { onDelete(task) }) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete), // Use your delete icon here
//                    contentDescription = "Delete Task",
//                    tint = Color.Unspecified // This ensures the icon keeps its original color
//                )
//            }
//        }
//    }
//}
//@Composable
//fun TaskItem(task: Task) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
//            Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
//            // Add more details as needed
//        }
//    }
//}
