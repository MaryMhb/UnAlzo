package ir.unalzo.utils

import androidx.compose.ui.Modifier

fun Modifier.ifThen(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        block()
    } else {
        this
    }
}