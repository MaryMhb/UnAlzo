package ir.unalzo.ui.screens.games.orientation

import androidx.lifecycle.viewModelScope
import ir.unalzo.base.BaseViewModel
import ir.unalzo.di.DI
import ir.unalzo.navigation.navigator.RootNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.get


class OrientationGameViewModel : BaseViewModel<OrientationGameState, OrientationGameEvent, Nothing>(
    defaultState = OrientationGameState()
) {
    override fun onEvent(event: OrientationGameEvent) {
        when (event) {
            OrientationGameEvent.ContinueGame -> continueGame()
            is OrientationGameEvent.PickOrientation -> pickOrientation(event.orientation)
            OrientationGameEvent.StartGame -> startGame()
            OrientationGameEvent.TryAgain -> tryAgain()
            OrientationGameEvent.StartPicking -> updateState { it.copy(scene = OrientationGameState.Scene.Pick) }
            OrientationGameEvent.Back -> back()
        }
    }

    private fun continueGame() {
        updateState { it.copy(gameLevel = state.value.gameLevel + 1) }
        startGame()
    }

    private fun back() {
        when (state.value.scene) {
            OrientationGameState.Scene.Beginning -> DI.get<RootNavigator>().navigateUp()
            else -> updateState { OrientationGameState() }
        }
    }

    private fun pickOrientation(orientation: OrientationGameState.GameOrientation) {
        viewModelScope.launch {
            val previousPick = state.value.userPickLevel
            val newLevel = (previousPick ?: -1) + 1
            println("pick level: $newLevel")
            updateState {
                it.copy(
                    selectedOrientationByUser = orientation,
                    userPickLevel = newLevel
                )
            }
            val correctOrientation = state.value.orientations[newLevel]
            val isWrong = orientation != correctOrientation.orientation
            delay(500)
            if (isWrong) {
                updateState {
                    it.copy(
                        scene = OrientationGameState.Scene.Result(false)
                    )
                }
            } else if (newLevel >= state.value.gameLevel - 1) { // all picked without being wrong
                updateState {
                    it.copy(
                        scene = OrientationGameState.Scene.Result(true)
                    )
                }
            } else {
                updateState {
                    it.copy(
                        selectedOrientationByUser = null,
                        shouldPickNextOrientation = true
                    )
                }
            }

        }
    }

    private fun startGame() {
        val keepOrientationAvatarFor = 2000L
        viewModelScope.launch {
            val orientations = List(state.value.gameLevel) {
                OrientationGameState.ShowOrientation(
                    id = it,
                    orientation = OrientationGameState.GameOrientation.entries.random()
                )
            }
            val newState = state.value.copy(
                scene = OrientationGameState.Scene.Show,
                orientations = orientations,
                currentVisibleOrientationCharacter = orientations.first(),
                selectedOrientationByUser = null,
                userPickLevel = null,
                shouldPickNextOrientation = false
            )
            updateState { newState }
            delay(keepOrientationAvatarFor)
            while (state.value.orientations.firstOrNull { it.id > state.value.currentVisibleOrientationCharacter?.id ?: 0 } != null) {
                updateState {
                    it.copy(
                        currentVisibleOrientationCharacter = state.value.orientations.firstOrNull { it.id > state.value.currentVisibleOrientationCharacter?.id ?: 0 }
                    )
                }
                delay(keepOrientationAvatarFor)
            }
            updateState {
                it.copy(
                    scene = OrientationGameState.Scene.YourTurn
                )
            }
        }
    }

    private fun tryAgain() {
        updateState { it.copy(gameLevel = 1) }
        startGame()
    }
}