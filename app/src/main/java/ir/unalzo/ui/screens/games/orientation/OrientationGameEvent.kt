package ir.unalzo.ui.screens.games.orientation

sealed interface OrientationGameEvent {
    data object StartGame : OrientationGameEvent
    data object ContinueGame : OrientationGameEvent
    data object TryAgain : OrientationGameEvent
    data class PickOrientation(val orientation: OrientationGameState.GameOrientation) :
        OrientationGameEvent

    data object StartPicking : OrientationGameEvent
    data object Back : OrientationGameEvent
}