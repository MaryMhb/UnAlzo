package ir.unalzo.ui.screens.games.imageGuess

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.unalzo.R
import ir.unalzo.base.AnimatedContentState
import ir.unalzo.base.collectViewModelState
import ir.unalzo.ui.components.Gif
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.ErrorNormal
import ir.unalzo.ui.theme.Orange500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.SuccessNormal
import ir.unalzo.ui.theme.screenPadding
import ir.unalzo.utils.ifThen
import kotlinx.coroutines.launch

@Composable
fun ImageGuessScreen(
    viewModel: ImageGuessViewModel = viewModel()
) {
    val state by viewModel.collectViewModelState()
    BackHandler() {
        viewModel.onEvent(ImageGuessEvent.Back)
    }
    ImageGuessContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ImageGuessContent(
    state: ImageGuessState,
    onEvent: (ImageGuessEvent) -> Unit
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
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    onEvent(ImageGuessEvent.Back)
                },
                modifier = Modifier
                    .padding(
                        top = screenPadding.calculateTopPadding(),
                        end = screenPadding.calculateStartPadding(
                            LocalLayoutDirection.current
                        )
                    )
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
            AnimatedContentState(
                state = state.gameState,
            ) { gameState ->
                when (gameState) {
                    is ImageGuessState.GameState.AtGame -> GameContent(gameState, onEvent)
                    ImageGuessState.GameState.AtLevelSelection -> LevelSelectionContent(
                        state,
                        onEvent
                    )

                    is ImageGuessState.GameState.AtResult -> ResultContent(gameState, onEvent)
                }
            }
        }
    }
}

@Composable
private fun GameContent(
    gameState: ImageGuessState.GameState.AtGame,
    onEvent: (ImageGuessEvent) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenPadding.calculateTopPadding(),
                horizontal = 48.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = gameState.level.question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(Modifier.height(18.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(gameState.level.getImageResource(context)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }


        Column {
            Spacer(Modifier.height(22.dp))
            gameState.level.options.forEach { option ->
                val isSelected = gameState.selectedOption == option
                val containerColor = when {
                    isSelected && gameState.isCorrect == true -> SuccessNormal
                    isSelected && gameState.isCorrect == false -> ErrorNormal
                    else -> Color.White
                }
                Box(
                    Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(containerColor)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = Blue500
                        )
                        .clickable {
                            onEvent(ImageGuessEvent.SelectOption(option))
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun LevelSelectionContent(
    state: ImageGuessState,
    onEvent: (ImageGuessEvent) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(state.lastPassedLevelId) {
        scope.launch {
            listState.animateScrollToItem(
                state.lastPassedLevelId,
            )
        }
    }
    println("last passed: ${state.lastPassedLevelId}, ${state.levels.map { it.isLocked(state.lastPassedLevelId) }}")
    BoxWithConstraints(Modifier, contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .height(maxHeight)
                .width(1.5.dp)
        ) {
//            Box(Modifier.fillMaxWidth().height(80.dp).background(Brush.verticalGradient(listOf(
//                Color.Transparent,
//                Color.White
//            ))))
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(100.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xff9D7CFF),
                            Color.Transparent
                        )
                    )
                )
                .zIndex(2f)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            reverseLayout = true,
            state = listState,
            contentPadding = PaddingValues(
                bottom = 32.dp
            ),
            flingBehavior = rememberSnapFlingBehavior(
                lazyListState = listState
            )
        ) {
            items(state.levels) { level ->
                val locked = level.isLocked(state.lastPassedLevelId)
                val contentColor = if (locked) Color(0xffAA8DFF) else Orange500
                Box(Modifier) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(100.dp))
                        Box(
                            Modifier
                                .size(90.dp)
                                .clickable {
                                    onEvent(ImageGuessEvent.SelectLevel(level))
                                }
                                .applyLevelPolygon(contentColor)
                                .ifThen(!locked) {
                                    padding(top = 6.dp)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (locked) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_lock),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            } else {
                                Text(
                                    text = level.label,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultContent(
    gameState: ImageGuessState.GameState.AtResult,
    onEvent: (ImageGuessEvent) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenPadding.calculateTopPadding(),
                horizontal = 48.dp
            ), verticalArrangement = Arrangement.Center
    ) {
        Gif(
            gif = R.drawable.congrats,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(10.dp)),
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = {
                if (gameState.canGoNext) {
                    onEvent(ImageGuessEvent.NextLevel)
                } else {
                    onEvent(ImageGuessEvent.BackToLevelSelection)
                }
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
                text = if (gameState.canGoNext) "مرحله بعدی" else "بازگشت",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(Modifier.height(120.dp))
    }
}

fun Modifier.applyLevelPolygon(color: Color): Modifier {
    return drawWithCache {
        val rounding = CornerRounding(
            radius = 16.dp.toPx(),
        )
        val roundedPolygon = RoundedPolygon(
            numVertices = 6,
            radius = (size.minDimension / 2),
            centerX = size.width / 2,
            centerY = size.height / 2,
            rounding = rounding
        )
        val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
        val roundedBorderPolygonPath = roundedPolygon.toPath().asComposePath()
        onDrawBehind {
            rotate(90f) {
                drawPath(
                    roundedBorderPolygonPath,
                    color = Color.White,
                    style = Stroke(
                        width = 2.dp.toPx()
                    )
                )
                drawPath(roundedPolygonPath, color = color)
            }

        }
    }
}


@Preview
@Composable
private fun ImagGuessPreview(
    @PreviewParameter(ImageGuessPreviewStateProvider::class)
    state: ImageGuessState
) {
    Prev {
        ImageGuessContent(
            state = state,
            onEvent = {

            }
        )
    }
}

private class ImageGuessPreviewStateProvider : PreviewParameterProvider<ImageGuessState> {
    override val values: Sequence<ImageGuessState>
        get() = sequenceOf(
            ImageGuessState(
                gameState = ImageGuessState.GameState.AtLevelSelection,
                levels = getFakeLevels(),
                lastPassedLevelId = 2
            ),
            ImageGuessState(
                gameState = ImageGuessState.GameState.AtGame(
                    level = getFakeLevels().first()
                ).apply {
                    isCorrect = true
                    selectedOption = "گزینه 2"
                },
                levels = getFakeLevels(),
                lastPassedLevelId = 2
            ),
            ImageGuessState(
                gameState = ImageGuessState.GameState.AtResult(false),
                levels = getFakeLevels(),
                lastPassedLevelId = 2
            ),
            ImageGuessState(
                gameState = ImageGuessState.GameState.AtResult(true),
                levels = getFakeLevels(),
                lastPassedLevelId = 2
            ),
        )

    fun getFakeLevels(): List<ImageGuessState.Level> {
        return List(5) {
            ImageGuessState.Level(
                id = it + 1,
                image = "image_guess_1",
                question = "این سوال است؟",
                options = List(4) {
                    "گزینه $it"
                },
                correctOption = "گزینه 2",
                label = it.toString()
            )
        }
    }

    override fun getDisplayName(index: Int): String? {
        return values.toList()[index].gameState::class.simpleName
    }

}