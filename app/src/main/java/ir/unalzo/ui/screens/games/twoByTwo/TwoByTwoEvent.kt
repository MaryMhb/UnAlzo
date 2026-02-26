package ir.unalzo.ui.screens.games.twoByTwo

sealed interface TwoByTwoEvent {
    data object StartGame : TwoByTwoEvent
    data object Back : TwoByTwoEvent
    data class OpenCard(val card: TwoByTwoState.GameCard) : TwoByTwoEvent
}