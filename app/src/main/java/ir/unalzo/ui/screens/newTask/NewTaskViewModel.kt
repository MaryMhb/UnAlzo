package ir.unalzo.ui.screens.newTask

import android.content.Context
import androidx.lifecycle.viewModelScope
import ir.unalzo.base.BaseViewModel
import ir.unalzo.database.dao.TaskDao
import ir.unalzo.database.entities.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject

data class NewTaskState(
    val titleFieldError: Boolean = false
)

sealed interface NewTaskEvent {
    data class SubmitTask(val taskEntity: TaskEntity) : NewTaskEvent
    object RemoveTitleError : NewTaskEvent
}

sealed interface NewTaskEffect {
    data object TaskAdded : NewTaskEffect
}

class NewTaskViewModel : BaseViewModel<NewTaskState, NewTaskEvent, NewTaskEffect>(
    defaultState = NewTaskState()
) {
    private val taskDao: TaskDao by inject()
    private val context: Context by inject()

    override fun onEvent(event: NewTaskEvent) {
        when (event) {
            is NewTaskEvent.SubmitTask -> submitTask(event.taskEntity)
            NewTaskEvent.RemoveTitleError -> removeTitleError()
        }
    }

    private fun submitTask(task: TaskEntity) {
        if (task.title.isEmpty()) {
            updateState { it.copy(titleFieldError = true) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val addedId = taskDao.addTask(task).toInt()
            val addedTask = task.copy(id = addedId)
            addedTask.schedule(context)
        }
        sendEffect(NewTaskEffect.TaskAdded)
    }

    private fun removeTitleError() {
        updateState { it.copy(titleFieldError = false) }
    }
}