package ir.unalzo.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import androidx.compose.runtime.State as ComposeState

abstract class BaseViewModel<State, Event, Effect>(
    defaultState: State
): ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(defaultState)
    val state = _state.asStateFlow()

    val effects = Channel<Effect>()

    fun updateState(update: (State) -> State) {
        _state.update { update(it) }
    }

    abstract fun onEvent(event: Event)

    fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            effects.send(effect)
        }
    }
}

@Composable
fun <State>BaseViewModel<State,*,*>.collectViewModelState(): ComposeState<State> {
    return state.collectAsState()
}

@Composable
fun <Effect> BaseViewModel<*, *, Effect>.collectViewModelEffects(onEffect: (Effect) -> Unit) {
    LaunchedEffect(Unit) {
        effects.receiveAsFlow().collect(onEffect)
    }
}