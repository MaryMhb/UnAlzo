package ir.unalzo.ui.screens.main.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import ir.unalzo.base.BaseViewModel
import ir.unalzo.database.dao.TaskDao
import ir.unalzo.database.entities.TaskEntity
import ir.unalzo.di.DI
import ir.unalzo.utils.Constants
import ir.unalzo.utils.get
import ir.unalzo.utils.sharedPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.core.component.inject

sealed interface HomeEvent {
    data class ToggleTask(val taskEntity: TaskEntity): HomeEvent
}

data class HomeState(
    val name: String,
    val undoneTasks: List<TaskEntity>? = null
)

class HomeViewModel: BaseViewModel<HomeState, HomeEvent, Unit>(
    defaultState = HomeState(
        name = DI.get<Context>().sharedPrefs.get<String>(Constants.Prefs.NAME)
    )
) {
    private val taskDao: TaskDao by inject()
    private val context: Context by inject()

    init {
        loadTasks()
    }

    override fun onEvent(event: HomeEvent) {
        when(event){
            is HomeEvent.ToggleTask -> toggleTask(event.taskEntity)
        }
    }

    private fun toggleTask(taskEntity: TaskEntity){
        viewModelScope.launch(Dispatchers.IO) {
            val newTask = taskEntity.copy(isDone = taskEntity.isDone.not())
            newTask.scheduleOrCancel(context)
            taskDao.updateTask(newTask)
        }
    }
    private fun loadTasks(){
        viewModelScope.launch {
            taskDao.getTasks().collect { result->
                updateState {
                    it.copy(
                        undoneTasks = result.filter { it.isDone.not() }
                    )
                }
            }
        }
    }
}