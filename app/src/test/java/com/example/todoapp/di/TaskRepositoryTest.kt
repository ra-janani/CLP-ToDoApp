package com.example.todoapp.di

import com.example.todoapp.data.dao.SubTaskDao
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.data.model.SubTask
import com.example.todoapp.data.model.Task
import com.google.android.gms.tasks.Task as FirebaseTask
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class TaskRepositoryTest {

    @Mock
    private lateinit var taskDao: TaskDao

    @Mock
    private lateinit var subTaskDao: SubTaskDao

    @Mock
    private lateinit var firestore: FirebaseFirestore

    @Mock
    private lateinit var tasksCollection: CollectionReference

    @Mock
    private lateinit var taskDocument: DocumentReference

    @Mock
    private lateinit var subTasksCollection: CollectionReference

    @Mock
    private lateinit var subTaskDocument: DocumentReference

    @Mock
    private lateinit var firestoreTask: FirebaseTask<Void>

    private lateinit var taskRepository: TaskRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        taskRepository = TaskRepository(taskDao, subTaskDao, firestore)

        `when`(firestore.collection("tasks")).thenReturn(tasksCollection)
        `when`(tasksCollection.document(any())).thenReturn(taskDocument)
        `when`(taskDocument.collection("subtasks")).thenReturn(subTasksCollection)
        `when`(subTasksCollection.document(any())).thenReturn(subTaskDocument)
        `when`(taskDocument.set(any())).thenReturn(firestoreTask)
        `when`(taskDocument.delete()).thenReturn(firestoreTask)
        `when`(subTaskDocument.set(any())).thenReturn(firestoreTask)
        `when`(subTaskDocument.delete()).thenReturn(firestoreTask)
        `when`(firestoreTask.isComplete).thenReturn(true)
        `when`(firestoreTask.isSuccessful).thenReturn(true)
    }

    @Test
    fun getAllTasks() = runTest {
        val tasks = listOf(Task(1, "Task 1","desc1"), Task(2, "Task 2","desc2"))
        `when`(taskDao.getAllTasks()).thenReturn(flowOf(tasks))

        val result = taskRepository.allTasks

        verify(taskDao).getAllTasks()
        assert(result == flowOf(tasks))
    }

    @Test
    fun getTaskById() = runTest {
        val task = Task(1, "Task 1","desc1")
        `when`(taskDao.getTaskById(1)).thenReturn(flowOf(task))

        val result = taskRepository.getTaskById(1)

        verify(taskDao).getTaskById(1)
        assert(result == flowOf(task))
    }

    @Test
    fun insertTask() = runTest {
        val task = Task(1, "Task 1","desc1")

        taskRepository.insertTask(task)

        verify(taskDao).insertTask(task)
        verify(taskDocument).set(task)
    }

    @Test
    fun updateTask() = runTest {
        val task = Task(1, "Updated Task","desc1")

        taskRepository.updateTask(task)

        verify(taskDao).updateTask(task)
        verify(taskDocument).set(task)
    }

    @Test
    fun deleteTask() = runTest {
        val task = Task(1, "Task to Delete","desc1")

        taskRepository.deleteTask(task)

        verify(taskDao).deleteTask(task)
        verify(taskDocument).delete()
    }

    @Test
    fun insertSubTask() = runTest {
        val subTask = SubTask(1, "Subtask1", false,1)

        taskRepository.insertSubTask(subTask)

        verify(subTaskDao).insertSubTask(subTask)
        verify(subTaskDocument).set(subTask)
    }

    @Test
    fun deleteSubTask() = runTest {
        val subTask = SubTask(1, "SubTask to Delete",false,1 )

        taskRepository.deleteSubTask(subTask)

        verify(subTaskDao).deleteSubTask(subTask)
        verify(subTaskDocument).delete()
    }

    @Test
    fun updateSubTask() = runTest {
        val subTask = SubTask(1, "Updated SubTask", false, 1 )

        taskRepository.updateSubTask(subTask)

        verify(subTaskDao).updateSubTask(subTask)
        verify(subTaskDocument).set(subTask)
    }

    @Test
    fun getSubTasksForTask() = runTest {
        val subTasks = listOf(SubTask(1, "SubTask 1",false,1  ), SubTask(2, "SubTask 2", false,1))
        `when`(subTaskDao.getSubTasksForTask(1)).thenReturn(flowOf(subTasks))

        val result = taskRepository.getSubTasksForTask(1)

        verify(subTaskDao).getSubTasksForTask(1)
        assert(result == flowOf(subTasks))
    }
}