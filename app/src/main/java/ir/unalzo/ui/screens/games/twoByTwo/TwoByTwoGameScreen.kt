package ir.unalzo.ui.screens.games.twoByTwo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.unalzo.R
import ir.unalzo.base.AnimatedContentState
import ir.unalzo.base.collectViewModelState
import ir.unalzo.models.Card
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.SuccessNormal
import ir.unalzo.ui.theme.screenPadding

@Composable
fun TwoByTwoGameScreen(viewModel: TwoByTwoViewModel = viewModel()) {
    val state by viewModel.collectViewModelState()
    BackHandler(onBack = {
        viewModel.onEvent(TwoByTwoEvent.Back)
    })
    TwoByTwoGameContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun TwoByTwoGameContent(
    state: TwoByTwoState,
    onEvent: (TwoByTwoEvent) -> Unit
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
                    onEvent(TwoByTwoEvent.Back)
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
                    TwoByTwoState.GameState.Beginning -> BeginningContent(onEvent)
                    is TwoByTwoState.GameState.Started -> GameStartedContent(gameState, onEvent)
                }
            }
        }

    }

}

@Composable
private fun BeginningContent(
    onEvent: (TwoByTwoEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 56.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SuccessNormal)
                    .padding(22.dp)
            ) {
                CardItem(
                    backgroundColor = Color.White,
                    icon = R.drawable.ic_hear,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                CardItem(
                    backgroundColor = Color.White,
                    icon = R.drawable.ic_hear,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
            Spacer(Modifier.height(32.dp))
            Text(
                text = "به خاطر بسپار\n" +
                        "و تصاویر یکسان رو پیدا کن!",
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(32.dp))
        }
        Button(
            onClick = {
                onEvent(TwoByTwoEvent.StartGame)
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
                fontSize = 20.sp
            )
        }
    }
}

@Composable
private fun GameStartedContent(
    gameState: TwoByTwoState.GameState.Started,
    onEvent: (TwoByTwoEvent) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(12.dp))
        AnimatedContent(
            gameState.isInPreview
        ) { inPreview ->
            Text(
                text = if (inPreview) {
                    "به خاطر بسپارید (${gameState.previewTimer} ثانیه)"
                } else {
                    "انتخاب کنید"
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 24.sp
            )
        }
        Spacer(Modifier.height(32.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(gameState.cards) { card ->
                CardItem(
                    modifier = Modifier.aspectRatio(1f),
                    backgroundColor = card.card.getComposeColor(),
                    icon = card.card.getIconResource(context),
                    opened = card in gameState.openedCards || card in gameState.matchedCards || gameState.isInPreview,
                    onClick = {
                        onEvent(TwoByTwoEvent.OpenCard(card))
                    }
                )
            }
        }
        AnimatedVisibility(
            visible = gameState.matchedCards.count() == gameState.cards.count()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(64.dp))
                Text(
                    text = "برنده شدید",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        onEvent(TwoByTwoEvent.StartGame)
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
                        text = "شروع دوباره",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    icon: Int,
    opened: Boolean = true,
    onClick: () -> Unit = {},
) {
    val degree by animateFloatAsState(
        targetValue = if (opened) 180f else 0f,
        animationSpec = tween(500)
    )
    Box(
        modifier
            .size(50.dp)
            .graphicsLayer {
                rotationY = degree
            }
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
    ) {

        if (degree > 90f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .padding(10.dp), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        },
                    tint = Color.Black
                )
            }
        } else {
            Box(
                modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xff1F4068))
            )
        }
    }
}

@Preview
@Composable
private fun TwoByTwoGamePrev() {
    Prev() {
        TwoByTwoGameContent(
            state = TwoByTwoState()
        ) { }
    }
}

@Preview
@Composable
private fun TwoByTwoGamePrev2() {
    Prev() {
        TwoByTwoGameContent(
            state = TwoByTwoState(
                gameState = TwoByTwoState.GameState.Started(
                    cards = List(16) {
                        TwoByTwoState.GameCard(
                            id = it,
                            card = Card(
                                id = it,
                                icon = "ic_moon",
                                color = "#313131"
                            )
                        )
                    },
                    matchedCards = emptyList(),
                    openedCards = emptyList()
                )
            )
        ) { }
    }
}