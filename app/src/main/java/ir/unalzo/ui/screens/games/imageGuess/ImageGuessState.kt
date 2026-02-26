package ir.unalzo.ui.screens.games.imageGuess

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.unalzo.base.AnimatedContentState
import kotlinx.serialization.Serializable

data class ImageGuessState(
    val gameState: GameState = GameState.AtLevelSelection,
    val levels: List<Level> = emptyList(),
    val lastPassedLevelId: Int = 0
) {
    sealed interface GameState : AnimatedContentState {

        data object AtLevelSelection : GameState {
            override val key: String
                get() = "level_selection"
        }

        data class AtGame(val level: Level) : GameState {
            override val key: String
                get() = "game"

            var isCorrect by mutableStateOf(null as Boolean?)
            var selectedOption by mutableStateOf(null as String?)
        }

        data class AtResult(
            val canGoNext: Boolean,
        ) : GameState {
            override val key: String
                get() = "result"
        }
    }

    @Serializable
    data class Level(
        val id: Int,
        val label: String,
        val image: String,
        val question: String,
        val options: List<String>,
        val correctOption: String
    ) {
        fun getImageResource(context: Context): Int {
            return context.resources.getIdentifier(image, "drawable", context.packageName)
        }

        fun isLocked(lastPassedLevelId: Int): Boolean {
            return (id - lastPassedLevelId) > 1
        }
    }
}