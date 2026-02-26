package ir.unalzo.ui.screens.games.twoByTwo

import ir.unalzo.base.AnimatedContentState
import ir.unalzo.models.Card

data class TwoByTwoState(
    val gameState: GameState = GameState.Beginning
) {
    sealed interface GameState : AnimatedContentState {
        data object Beginning : GameState {
            override val key: String
                get() = "beginning"
        }

        data class Started(
            val isInPreview: Boolean = true,
            val previewTimer: Int = 5,
            val cards: List<GameCard>,
            val matchedCards: List<GameCard>,
            val openedCards: List<GameCard>
        ) : GameState {
            override val key: String
                get() = "started"
        }

    }

    data class GameCard(
        val id: Int,
        val card: Card
    )
}