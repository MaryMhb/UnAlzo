package ir.unalzo.ui.screens.games.imageGuess

sealed interface ImageGuessEvent {
    data class SelectLevel(val level: ImageGuessState.Level) : ImageGuessEvent
    data class SelectOption(val option: String) : ImageGuessEvent
    data object NextLevel : ImageGuessEvent
    data object ReloadLevels : ImageGuessEvent
    data object BackToLevelSelection : ImageGuessEvent
    data object Back : ImageGuessEvent
}