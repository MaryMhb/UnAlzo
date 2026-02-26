package ir.unalzo.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

operator fun TextUnit.plus(other: TextUnit): TextUnit {
    return (value + other.value).sp
}