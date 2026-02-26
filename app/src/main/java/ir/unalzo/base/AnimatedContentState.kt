package ir.unalzo.base

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable

interface AnimatedContentState {
    val key: String
}

@Composable
fun <T : AnimatedContentState> AnimatedContentState(
    state: T,
    content: @Composable (T) -> Unit
) {
    AnimatedContent(
        targetState = state,
        contentKey = {
            state.key
        }
    ) {
        content(it)
    }
}