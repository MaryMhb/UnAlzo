package ir.unalzo.ui.screens.games.imageGuess

import android.content.Context
import androidx.lifecycle.viewModelScope
import ir.unalzo.R
import ir.unalzo.base.BaseViewModel
import ir.unalzo.di.DI
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.utils.Constants
import ir.unalzo.utils.set
import ir.unalzo.utils.sharedPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.get
import org.koin.core.component.inject

class ImageGuessViewModel : BaseViewModel<ImageGuessState, ImageGuessEvent, Nothing>(
    defaultState = ImageGuessState()
) {
    private val json: Json by inject()
    private val context: Context by inject()

    init {
        reloadLevels()
    }

    override fun onEvent(event: ImageGuessEvent) {
        when (event) {
            ImageGuessEvent.NextLevel -> nextLevel()
            ImageGuessEvent.ReloadLevels -> reloadLevels()
            is ImageGuessEvent.SelectLevel -> selectLevel(event.level)
            is ImageGuessEvent.SelectOption -> selectOption(event.option)
            ImageGuessEvent.BackToLevelSelection -> reloadLevels()
            ImageGuessEvent.Back -> back()
        }
    }

    private fun selectLevel(level: ImageGuessState.Level) {
        // check if locked or not
        if (level.isLocked(state.value.lastPassedLevelId).not()) {
            updateState {
                it.copy(
                    gameState = ImageGuessState.GameState.AtGame(level)
                )
            }
        }

    }

    private fun back() {
        when (state.value.gameState) {
            is ImageGuessState.GameState.AtGame -> reloadLevels()
            ImageGuessState.GameState.AtLevelSelection -> DI.get<RootNavigator>().navigateUp()
            is ImageGuessState.GameState.AtResult -> reloadLevels()
        }
    }

    private fun selectOption(option: String) {
        (state.value.gameState as? ImageGuessState.GameState.AtGame)?.let { gameState ->
            viewModelScope.launch {
                val isCorrect = option == gameState.level.correctOption
                gameState.isCorrect = isCorrect
                gameState.selectedOption = option
                if (isCorrect) {
                    delay(500)
                    context.sharedPrefs[Constants.Prefs.LAST_IMAGE_GUESS_LEVEL_PASSED] =
                        gameState.level.id
                    updateState {
                        it.copy(
                            gameState = ImageGuessState.GameState.AtResult(
                                canGoNext = getNextLevel(
                                    lastPassedLevelId = gameState.level.id
                                ) != null
                            ),
                            lastPassedLevelId = gameState.level.id
                        )
                    }
                }
            }
        }
    }

    private fun nextLevel() {
        getNextLevel()?.let { nextLevel ->
            selectLevel(nextLevel)
        }
    }

    private fun getNextLevel(lastPassedLevelId: Int = state.value.lastPassedLevelId): ImageGuessState.Level? {
        val nextLevel = state.value.levels.firstOrNull { it.id > lastPassedLevelId }
        return nextLevel
    }

    private fun reloadLevels() {
        val levels: List<ImageGuessState.Level> =
            context.resources.openRawResource(R.raw.image_guess_levels)
                .bufferedReader()
                .readText().let {
                    json.decodeFromString(it)
                }
        val lastPassedLevel =
            context.sharedPrefs.getInt(Constants.Prefs.LAST_IMAGE_GUESS_LEVEL_PASSED, 0)

        updateState {
            it.copy(
                levels = levels,
                lastPassedLevelId = lastPassedLevel,
                gameState = ImageGuessState.GameState.AtLevelSelection
            )
        }
    }
}
