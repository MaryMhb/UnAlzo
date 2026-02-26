package ir.unalzo.ui.screens.games.twoByTwo

import android.content.Context
import androidx.lifecycle.viewModelScope
import ir.unalzo.R
import ir.unalzo.base.BaseViewModel
import ir.unalzo.di.DI
import ir.unalzo.models.Card
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.utils.isAllSame
import ir.unalzo.utils.times
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.get
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds


class TwoByTwoViewModel : BaseViewModel<TwoByTwoState, TwoByTwoEvent, Nothing>(
    defaultState = TwoByTwoState()
) {
    private val context: Context by inject()
    private val json: Json by inject()

    private var gameJob: Job? = null

    override fun onEvent(event: TwoByTwoEvent) {
        when (event) {
            TwoByTwoEvent.Back -> back()
            is TwoByTwoEvent.OpenCard -> openCard(event.card)
            TwoByTwoEvent.StartGame -> startGame()
        }
    }

    private fun back() {
        gameJob?.cancel()
        when (state.value.gameState) {
            TwoByTwoState.GameState.Beginning -> DI.get<RootNavigator>().navigateUp()
            is TwoByTwoState.GameState.Started -> updateState {
                it.copy(
                    gameState = TwoByTwoState.GameState.Beginning
                )
            }
        }
    }

    private fun openCard(card: TwoByTwoState.GameCard) {
        println("openCard")
        gameJob = viewModelScope.launch {
            (state.value.gameState as? TwoByTwoState.GameState.Started)?.let { gameState ->
                if (gameState.isInPreview) return@launch
                if (card in gameState.matchedCards || card in gameState.openedCards) return@launch
                if (gameState.openedCards.count() == 2) return@launch
                val newOpenedCards = gameState.openedCards + card
                var newState = gameState
                if (newOpenedCards.count() == 2) {
                    val isSameCardsOpened = newOpenedCards
                        .isAllSame { it.card.id }
                    if (isSameCardsOpened) {
                        newState = newState.copy(
                            matchedCards = newState.matchedCards + newOpenedCards,
                            openedCards = emptyList()
                        )
                    } else {
                        newState = newState.copy(
                            openedCards = newOpenedCards
                        )
                        updateState { it.copy(gameState = newState) }
                        delay(1.seconds)
                        newState = newState.copy(
                            openedCards = emptyList()
                        )
                        updateState { it.copy(gameState = newState) }
                    }
                } else {
                    println("plus to opened cards")
                    newState = newState.copy(
                        openedCards = newOpenedCards
                    )
                }
                updateState { it.copy(gameState = newState) }
            }
        }
    }

    private fun startGame() {
        val availableCards: List<Card> =
            context.resources.openRawResource(R.raw.two_by_two_game_cards)
                .bufferedReader()
                .readText().let {
                    json.decodeFromString(it)
                }
        val gameCards = (availableCards * 2)
            .mapIndexed { index, card -> TwoByTwoState.GameCard(id = index, card = card) }
            .shuffled()

        val newGameState = TwoByTwoState.GameState.Started(
            isInPreview = true,
            cards = gameCards,
            matchedCards = emptyList(),
            openedCards = emptyList(),
            previewTimer = 5
        )
        gameJob = viewModelScope.launch {
            updateState {
                it.copy(gameState = newGameState)
            }
            repeat(5) {
                delay(1.seconds)
                updateState {
                    it.copy(
                        gameState = newGameState.copy(
                            previewTimer = (state.value.gameState as TwoByTwoState.GameState.Started).previewTimer - 1
                        )
                    )
                }
            }
            updateState {
                it.copy(gameState = newGameState.copy(isInPreview = false, previewTimer = 0))
            }
        }
    }
}