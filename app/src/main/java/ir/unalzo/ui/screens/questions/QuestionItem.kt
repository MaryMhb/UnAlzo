package ir.unalzo.ui.screens.questions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed interface QuestionItem {
    sealed interface Answerable {
        val answer: MutableState<String>

        val optional: Boolean
    }

    data object Start : QuestionItem
    data class Input(
        val title: String,
        val placeholder: String,
    ) : QuestionItem, Answerable{
        override val answer: MutableState<String> = mutableStateOf("")
        override val optional: Boolean = true
    }

    data class Option(
        val title: String,
        val options: List<String>,
    ) : QuestionItem, Answerable{
        override val answer: MutableState<String> = mutableStateOf("")
        override val optional: Boolean = true
    }
}