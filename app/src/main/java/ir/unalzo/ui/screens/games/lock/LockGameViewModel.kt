package ir.unalzo.ui.screens.games.lock

import ir.unalzo.base.BaseViewModel

data class LockGameState(
    val correctDigits: List<Int> = getRandomPassword(),
    val enteredDigits: List<Int> = emptyList(),
    val lockState: LockState? = null,
) {
    sealed interface LockState {
        data object AllWrong : LockState
        data class SomeDigitsIsOk(val digits: Int) : LockState
        data object Unlocked : LockState
    }
}

sealed interface LockGameEvent {
    data class EnterDigit(val number: Int) : LockGameEvent
    data object NewGame : LockGameEvent
    data object ResetEntries : LockGameEvent
    data object Retry : LockGameEvent
    data object SubmitPassword : LockGameEvent
}

class LockGameViewModel : BaseViewModel<LockGameState, LockGameEvent, Nothing>(
    defaultState = LockGameState()
) {
    override fun onEvent(event: LockGameEvent) {
        when (event) {
            is LockGameEvent.EnterDigit -> enterDigit(event.number)
            LockGameEvent.NewGame -> newGame()
            LockGameEvent.ResetEntries -> updateState { it.copy(enteredDigits = emptyList()) }
            LockGameEvent.Retry -> updateState {
                it.copy(
                    lockState = null,
                    enteredDigits = emptyList()
                )
            }

            LockGameEvent.SubmitPassword -> submitPassword()
        }
    }

    private fun enterDigit(number: Int) {
        if (state.value.enteredDigits.count() < state.value.correctDigits.count()) {
            updateState { it.copy(enteredDigits = it.enteredDigits + number) }
        }
    }

    private fun newGame() {
        updateState {
            it.copy(
                lockState = null,
                enteredDigits = emptyList(),
                correctDigits = getRandomPassword()
            )
        }
    }

    private fun submitPassword() {
        val correctDigitsCount = getCorrectDigitsCount(
            entered = state.value.enteredDigits,
            correct = state.value.correctDigits
        )
        val lockState = when {
            correctDigitsCount == state.value.correctDigits.count() -> {
                LockGameState.LockState.Unlocked
            }

            correctDigitsCount > 0 -> {
                LockGameState.LockState.SomeDigitsIsOk(correctDigitsCount)
            }

            else -> LockGameState.LockState.AllWrong
        }
        updateState {
            it.copy(
                lockState = lockState
            )
        }
    }

    private fun getCorrectDigitsCount(
        entered: List<Int>,
        correct: List<Int>
    ): Int {
        var result = 0
        entered.forEachIndexed { index, digit ->
            val correctDigit = correct[index]
            if (digit == correctDigit) {
                result++
            }
        }
        return result
    }
}

private fun getRandomPassword(count: Int = 3) = List(count) {
    (0..9).random()
}
