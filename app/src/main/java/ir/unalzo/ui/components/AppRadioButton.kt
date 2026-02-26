package ir.unalzo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.unalzo.ui.theme.Pink500

@Composable
fun AppRadioButton(
    checked: Boolean,
) {
    Box(
        Modifier
            .size(34.dp)
            .border(
                width = 3.dp,
                shape = CircleShape,
                color = Pink500
            )
            .padding(6.dp)
    ) {
        AnimatedVisibility(
            visible = checked,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Box(Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Pink500))
        }
    }
}