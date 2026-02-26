package ir.unalzo.ui.screens.games.orientation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.unalzo.R
import ir.unalzo.base.AnimatedContentState
import ir.unalzo.base.collectViewModelState
import ir.unalzo.ui.components.Gif
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.screenPadding
import ir.unalzo.utils.ifThen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrientationGameScreen(
    viewModel: OrientationGameViewModel = viewModel()
) {
    val state by viewModel.collectViewModelState()
    BackHandler {
        viewModel.onEvent(OrientationGameEvent.Back)
    }
    OrientationGameContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun OrientationGameContent(
    state: OrientationGameState,
    onEvent: (OrientationGameEvent) -> Unit,
) {
    GameUi(onEvent) {
        AnimatedContentState(
            state.scene,
        ) {
            when (it) {
                OrientationGameState.Scene.Beginning -> BeginningScene(onEvent)
                OrientationGameState.Scene.Pick -> PickOrientationScene(state, onEvent)
                is OrientationGameState.Scene.Result -> ResultScene(it.correct, onEvent)
                OrientationGameState.Scene.Show -> ShowScene(state)
                OrientationGameState.Scene.YourTurn -> YourTurnScene(onEvent)
            }
        }
    }
}

@Composable
private fun ShowScene(
    state: OrientationGameState,
) {
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visibleState = remember {
                MutableTransitionState(false).apply {
                    scope.launch {
                        delay(200)
                        targetState = true
                    }
                }
            },
            enter = fadeIn(),
            modifier = Modifier.zIndex(3f)
        ) {
            Image(
                painter = painterResource(R.drawable.show_light),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            state.currentVisibleOrientationCharacter?.let { character ->
                AnimatedContent(
                    targetState = character,
                    modifier = Modifier
                        .offset(
                            y = 64.dp
                        )
                        .zIndex(2f)
                ) {
                    Image(
                        painter = painterResource(it.orientation.character),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5 / 5f),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.img_stage),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
                    .padding(horizontal = 32.dp),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun BeginningScene(
    onEvent: (OrientationGameEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 32.dp,
                vertical = 48.dp
            ), verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            GameButton(
                label = "با دقت ببین",
                onClick = {

                }
            )
            Image(
                painter = painterResource(R.drawable.img_hand),
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(4 / 5f),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            GameButton(
                label = "تکرار کن!",
                onClick = {

                }
            )
            Image(
                painter = painterResource(R.drawable.arrow_left),
                modifier = Modifier
                    .width(130.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }
        Button(
            onClick = {
                onEvent(OrientationGameEvent.StartGame)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue500
            ),
            contentPadding = PaddingValues(
                vertical = 14.dp
            )
        ) {
            Text(
                text = "شروع",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun GameButton(
    label: String,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        Modifier
            .clip(shape)
            .background(Color.White)
            .border(
                width = 1.5.dp,
                color = Color.Black,
                shape = shape
            )
            .clickable { onClick() }
            .padding(horizontal = 28.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}

@Composable
fun ResultScene(
    isCorrect: Boolean,
    onEvent: (OrientationGameEvent) -> Unit
) {
    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Gif(
            gif = if (isCorrect) R.drawable.gif_orientation_game_correct else R.drawable.gif_orientation_game_incorrect,
            modifier = Modifier
                .ifThen(isCorrect) {
                    size(270.dp, 200.dp)
                }
                .ifThen(isCorrect.not()) {
                    size(200.dp)
                },
            contentScale = ContentScale.FillBounds
        )
        Spacer(Modifier.height(48.dp))
        val text = if (isCorrect) "آفرین" else "جهت اشتباه"
        Box(contentAlignment = Alignment.Center) {
            val baseSize = 60.sp
            Text(
                text = text,
                fontSize = baseSize,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    drawStyle = Stroke(
                        width = with(density) {
                            38.dp.toPx()
                        },
                        pathEffect = PathEffect.cornerPathEffect(
                            with(density) {
                                8.dp.toPx()
                            }
                        ),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    ),
                    color = Color(0xffFFE11C),
                )
            )
            Text(
                text = text,
                fontSize = baseSize,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    drawStyle = Stroke(
                        width = with(density) {
                            18.dp.toPx()
                        },
                        pathEffect = PathEffect.cornerPathEffect(
                            with(density) {
                                8.dp.toPx()
                            }
                        )
                    ),
                    color = Color.Black,
                )
            )
            Text(
                text = text,
                fontSize = baseSize,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(48.dp))
        GameButton(
            label = if (isCorrect) "ادامه بدیم؟" else "تلاش دوباره"
        ) {
            if (isCorrect) {
                onEvent(OrientationGameEvent.ContinueGame)
            } else {
                onEvent(OrientationGameEvent.TryAgain)
            }
        }
        Spacer(Modifier.height(48.dp))

    }
}

@Composable
fun PickOrientationScene(state: OrientationGameState, onEvent: (OrientationGameEvent) -> Unit) {
    BoxWithConstraints() {
        val height = maxHeight
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = state.shouldPickNextOrientation
            ) {
                Text(
                    "جهت بعدی را انتخاب کنید",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
            Box(Modifier.size(200.dp)) {
                state.selectedOrientationByUser?.let { orientationByUser ->
                    Image(
                        painter = painterResource(orientationByUser.arrow),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(height / 5))
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OrientationButton(
                    rotation = 90f,
                    onClick = {
                        onEvent(OrientationGameEvent.PickOrientation(OrientationGameState.GameOrientation.Top))
                    },
                    selected = state.selectedOrientationByUser == OrientationGameState.GameOrientation.Top
                )
                Spacer(Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OrientationButton(
                        rotation = 180f,
                        onClick = {
                            onEvent(OrientationGameEvent.PickOrientation(OrientationGameState.GameOrientation.Right))
                        },
                        selected = state.selectedOrientationByUser == OrientationGameState.GameOrientation.Right
                    )
                    OrientationButton(
                        rotation = -90f,
                        onClick = {
                            onEvent(OrientationGameEvent.PickOrientation(OrientationGameState.GameOrientation.Bottom))
                        },
                        selected = state.selectedOrientationByUser == OrientationGameState.GameOrientation.Bottom
                    )
                    OrientationButton(
                        rotation = 0f,
                        onClick = {
                            onEvent(OrientationGameEvent.PickOrientation(OrientationGameState.GameOrientation.Left))
                        },
                        selected = state.selectedOrientationByUser == OrientationGameState.GameOrientation.Left
                    )
                }
            }
        }
    }
}

@Composable
fun OrientationButton(
    rotation: Float,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    val borderWidth = 8.dp
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color(0xffFFE11C) else Color.White
    )
    val shadowColor by animateColorAsState(
        targetValue = if (selected) Color(0xFFEEAE00) else Color(0xFFC7C7C7)
    )
    Box(
        Modifier
            .size(70.dp)
            .background(backgroundColor)
            .drawWithContent {
                val shadowWidth = 7.dp.toPx()
                val shadowSlope = 6.dp.toPx()
                val shadowPath1 = Path().apply {
                    moveTo(borderWidth.toPx(), size.height - borderWidth.toPx())
                    lineTo(shadowWidth + borderWidth.toPx(), size.height - borderWidth.toPx())
                    lineTo(shadowWidth + borderWidth.toPx(), shadowSlope + borderWidth.toPx())
                    lineTo(borderWidth.toPx(), borderWidth.toPx())
                    close()
                }
                val shadowPath2 = Path().apply {
                    moveTo(borderWidth.toPx(), size.height - borderWidth.toPx())
                    lineTo(size.width - borderWidth.toPx(), size.height - borderWidth.toPx())
                    lineTo(
                        size.width - borderWidth.toPx() - shadowWidth,
                        size.height - borderWidth.toPx() - shadowWidth
                    )
                    lineTo(
                        borderWidth.toPx() + shadowWidth,
                        size.height - borderWidth.toPx() - shadowWidth
                    )
                    close()
                }
                val shadowPath3 = Path().apply {
                    moveTo(size.width - borderWidth.toPx(), size.height - borderWidth.toPx())
                    lineTo(size.width - borderWidth.toPx(), borderWidth.toPx())
                    lineTo(
                        size.width - borderWidth.toPx() - shadowWidth,
                        borderWidth.toPx() + shadowWidth
                    )
                    lineTo(
                        size.width - borderWidth.toPx() - shadowWidth,
                        size.height - borderWidth.toPx() - shadowWidth
                    )
                    close()
                }
                drawPath(
                    path = shadowPath1,
                    color = shadowColor.copy(.5f)
                )
                drawPath(
                    path = shadowPath2,
                    color = shadowColor
                )
                drawPath(
                    path = shadowPath3,
                    color = shadowColor.copy(.5f)
                )
                drawContent()
            }
            .border(
                width = borderWidth,
                color = Color.Black
            )
            .clickable {
                onClick()
            }
            .padding(20.dp)
    ) {
        if (selected.not()) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(
                        y = 3.dp,
                    )
                    .rotate(rotation),
                tint = Color(0xffCFCFCF)
            )
        }
        Image(
            painter = painterResource(if (selected) R.drawable.ic_chevron_selected else R.drawable.ic_chevron),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation),
        )

    }
}

@Composable
fun YourTurnScene(
    onEvent: (OrientationGameEvent) -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val height = maxHeight
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_game_orientation_sm),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(height / 5))
            GameButton(
                label = "نوبت شماست!"
            ) {
                onEvent(OrientationGameEvent.StartPicking)
            }
        }
    }
}

@Composable
private fun GameUi(
    onEvent: (OrientationGameEvent) -> Unit,
    content: @Composable () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.lock_game_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = {
                onEvent(OrientationGameEvent.Back)
            },
            modifier = Modifier
                .padding(
                    top = screenPadding.calculateTopPadding(),
                    end = screenPadding.calculateStartPadding(
                        LocalLayoutDirection.current
                    )
                )
                .statusBarsPadding()
                .size(50.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = Blue500,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(Modifier.systemBarsPadding()) {
            content()
        }
    }
}

@Preview
@Composable
private fun OrientationGamePreview(
    @PreviewParameter(OrientationGameStateProvider::class)
    state: OrientationGameState,
) {
    Prev {
        OrientationGameContent(state, {})
    }
}

class OrientationGameStateProvider : PreviewParameterProvider<OrientationGameState> {
    override val values: Sequence<OrientationGameState>
        get() = sequenceOf(
            OrientationGameState(),
            OrientationGameState(
                scene = OrientationGameState.Scene.Show,
                orientations = listOf(
                    OrientationGameState.ShowOrientation(
                        id = 1,
                        orientation = OrientationGameState.GameOrientation.Top
                    )
                ),
                currentVisibleOrientationCharacter = OrientationGameState.ShowOrientation(
                    id = 1,
                    orientation = OrientationGameState.GameOrientation.Right
                )
            ),
            OrientationGameState(
                scene = OrientationGameState.Scene.YourTurn,
            ),
            OrientationGameState(
                scene = OrientationGameState.Scene.Pick,
                selectedOrientationByUser = OrientationGameState.GameOrientation.Top,
                shouldPickNextOrientation = true
            ),
            OrientationGameState(
                scene = OrientationGameState.Scene.Result(true),
            ),
            OrientationGameState(
                scene = OrientationGameState.Scene.Result(false),
            ),
        )

    override fun getDisplayName(index: Int): String? {
        return values.toList()[index].scene::class.simpleName
    }
}