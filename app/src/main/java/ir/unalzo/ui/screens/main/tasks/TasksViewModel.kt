package ir.unalzo.ui.screens.main.tasks

import android.content.Context
import androidx.lifecycle.viewModelScope
import ir.unalzo.base.BaseViewModel
import ir.unalzo.database.dao.TaskDao
import ir.unalzo.database.entities.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject


data class TasksState(
    val tasks: List<TaskEntity>? = null,
)

sealed interface TasksEvent {
    data class ToggleTask(val taskEntity: TaskEntity) : TasksEvent
}

class TasksViewModel : BaseViewModel<TasksState, TasksEvent, Nothing>(
    defaultState = TasksState()
) {
    private val taskDao: TaskDao by inject()
    private val context: Context by inject()

    init {
        loadTasks()
    }

    override fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.ToggleTask -> toggleTask(event.taskEntity)
        }
    }

    private fun toggleTask(taskEntity: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTask = taskEntity.copy(isDone = taskEntity.isDone.not())
            newTask.scheduleOrCancel(context)
            taskDao.updateTask(newTask)
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskDao.getTasks().collect { result ->
                updateState {
                    it.copy(
                        tasks = result
                    )
                }
            }
        }
    }
}