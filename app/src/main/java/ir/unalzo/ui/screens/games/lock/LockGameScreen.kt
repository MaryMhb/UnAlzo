package ir.unalzo.ui.screens.games.lock

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.unalzo.R
import ir.unalzo.base.collectViewModelState
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.ui.theme.Blue100
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.ErrorDark
import ir.unalzo.ui.theme.ErrorLight
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.SuccessDark
import ir.unalzo.ui.theme.SuccessLight
import ir.unalzo.ui.theme.WarningDark
import ir.unalzo.ui.theme.WarningLight
import ir.unalzo.ui.theme.poppins
import ir.unalzo.ui.theme.screenPadding
import org.koin.compose.koinInject

@Composable
fun LockGameScreen(
    viewModel: LockGameViewModel = viewModel()
) {
    val state by viewModel.collectViewModelState()
    println("game state: $state")
    LockGameContent(
        state = state,
        onEvent = viewModel::onEvent
    ) 
}

@Composable
private fun LockGameContent(
    state: LockGameState,
    navigator: RootNavigator = koinInject(),
    onEvent: (LockGameEvent) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.lock_game_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding)
                .systemBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = navigator::navigateUp,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Lock(
                state = state
            )
            Spacer(Modifier.height(24.dp))
            LockActions(
                state = state,
                onEvent = onEvent
            )
            Spacer(Modifier.height(32.dp))
            AnimatedContent(
                targetState = state.lockState != null,
                transitionSpec = {
                    fadeIn() + scaleIn() togetherWith scaleOut() + fadeOut()
                }
            ) { stateSpecified ->
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    if (stateSpecified) {
                        state.lockState?.let {
                            StateMessage(
                                lockState = state.lockState
                            )
                        }
                    } else {
                        Keyboard(
                            onKeyPressed = {
                                onEvent(LockGameEvent.EnterDigit(it))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Keyboard(
    onKeyPressed: (Int) -> Unit,
) {
    val keyboard = listOf(
        9, 8, 7, 6, 5, 4, 3, 2, 1, null, 0, null
    )
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 64.dp)
            .height(360.dp),
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(keyboard) { key ->
            key?.let {
                Box(
                    Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Blue100)
                        .clickable {
                            onKeyPressed(it)
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = key.toString(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins,
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StateMessage(lockState: LockGameState.LockState) {
    val containerColor: Color
    val borderColor: Color
    val text: String
    when (lockState) {
        LockGameState.LockState.AllWrong -> {
            containerColor = ErrorLight
            borderColor = ErrorDark
            text = "تمام اعداد غلط هستند."
        }

        is LockGameState.LockState.SomeDigitsIsOk -> {
            containerColor = WarningLight
            borderColor = WarningDark
            text = "${lockState.digits} رقم درست و در جای درست است."
        }

        LockGameState.LockState.Unlocked -> {
            containerColor = SuccessLight
            borderColor = SuccessDark
            text = "قفل باز شد."
        }
    }
    MessageCard(
        message = text,
        containerColor = containerColor,
        borderColor = borderColor
    )
}

@Composable
fun MessageCard(
    message: String,
    containerColor: Color,
    borderColor: Color
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        Modifier
            .clip(shape)
            .background(containerColor)
            .border(
                width = 2.dp,
                shape = shape,
                color = borderColor
            )
            .padding(horizontal = 22.dp, vertical = 8.dp)
    ) {
        Text(
            text = message,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun Lock(
    state: LockGameState
) {
    BoxWithConstraints(
        Modifier
            .width(200.dp)
            .height(260.dp)
    ) {
        AnimatedContent(
            targetState = state.lockState is LockGameState.LockState.Unlocked
        ) { unlocked ->
            val image = if (unlocked) R.drawable.img_lock_on else R.drawable.img_lock_off
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset(y = maxHeight / 6),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val rowScope = this
            state.correctDigits.forEachIndexed { index, _ ->
                val enteredDigit =
                    state.enteredDigits.getOrNull(state.correctDigits.lastIndex - index)
                Box(
                    Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    rowScope.AnimatedVisibility(
                        visible = enteredDigit != null,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut(),
                        modifier = Modifier.offset(y = 2.dp)
                    ) {
                        enteredDigit?.let {
                            Text(
                                text = it.toString(),
                                fontWeight = FontWeight.Black,
                                fontSize = 25.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LockActions(
    state: LockGameState,
    onEvent: (LockGameEvent) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        LockAction(
            text = "ثبت",
            onClick = {
                onEvent(LockGameEvent.SubmitPassword)
            }
        )
        AnimatedVisibility(
            visible = state.lockState != null
        ) {
            LockAction(
                text = "بازی جدید",
                onClick = {
                    onEvent(LockGameEvent.NewGame)
                }
            )
        }
        AnimatedVisibility(
            visible = state.lockState != null && (state.lockState is LockGameState.LockState.AllWrong || state.lockState is LockGameState.LockState.SomeDigitsIsOk)
        ) {
            LockAction(
                text = "تلاش مجدد",
                onClick = {
                    onEvent(LockGameEvent.Retry)
                }
            )
        }
        AnimatedVisibility(
            visible = state.lockState == null
        ) {
            LockAction(
                icon = Icons.Default.Refresh,
                onClick = {
                    onEvent(LockGameEvent.ResetEntries)
                }
            )
        }
    }
}

@Composable
private fun LockAction(
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        Modifier
            .padding(horizontal = 12.dp)
            .width(90.dp)
            .height(35.dp)
            .clip(shape)
            .background(Color(0xffd9d9d9))
            .border(
                width = 2.dp,
                color = Color.Black.copy(.5f),
                shape = shape
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun LockAction(
    icon: ImageVector,
    onClick: () -> Unit
) {
    LockAction(
        content = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
        },
        onClick = onClick
    )
}

@Composable
private fun LockAction(
    text: String,
    onClick: () -> Unit
) {
    LockAction(
        content = {
            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        },
        onClick = onClick
    )
}

@Preview
@Composable
private fun LockGamePrev() {
    Prev() {
        LockGameContent(
            state = LockGameState(
                enteredDigits = listOf(2, 3)
            ),
            onEvent = {

            }
        )
    }
}

@Preview
@Composable
private fun LockGamePrev2() {
    Prev() {
        LockGameContent(
            state = LockGameState(
                enteredDigits = listOf(2, 3),
                lockState = LockGameState.LockState.Unlocked
            ),
            onEvent = {

            }
        )
    }
}

@Preview
@Composable
private fun LockGamePrev3() {
    Prev() {
        LockGameContent(
            state = LockGameState(
                enteredDigits = listOf(2, 3),
                lockState = LockGameState.LockState.SomeDigitsIsOk(2)
            ),
            onEvent = {

            }
        )
    }
}

@Preview
@Composable
private fun LockGamePrev4() {
    Prev() {
        LockGameContent(
            state = LockGameState(
                enteredDigits = listOf(2, 3),
                lockState = LockGameState.LockState.AllWrong
            ),
            onEvent = {

            }
        )
    }
}