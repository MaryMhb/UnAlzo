package ir.unalzo.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.unalzo.R
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.navigation.routes.RootRoute
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.Purple500
import ir.unalzo.ui.theme.screenPadding
import org.koin.compose.koinInject

private data class GameItem(
    val image: Int,
    val name: String,
    val route: RootRoute
)

@Composable
fun GameScreen(
    navigator: RootNavigator = koinInject()
) {
    val games = listOf(
        GameItem(
            image = R.drawable.ic_game_orientation_sm,
            name = "جهت یابی",
            route = RootRoute.OrientationGame
        ),
        GameItem(
            image = R.drawable.ic_game_lock_sm,
            name = "قفل و کلید",
            route = RootRoute.LockGame
        ),
        GameItem(
            image = R.drawable.ic_game_two_by_two_sm,
            name = "دو به دو",
            route = RootRoute.TwoByTwoGame
        ),
        GameItem(
            image = R.drawable.ic_game_image_guess_sm,
            name = "حدس عکس",
            route = RootRoute.ImageGuessGame
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("بازی کن!", fontSize = 50.sp, color = Blue500, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(64.dp))
        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(48.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            items(games) {
                GameItem(
                    item = it,
                    onClick = {
                        navigator.navigate(it.route)
                    }
                )
            }
        }
        Spacer(Modifier.height(64.dp))
    }
}

@Composable
private fun GameItem(
    item: GameItem,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Purple500), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(item.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            )
        }
        val shape = RoundedCornerShape(10.dp)
        Box(
            Modifier
                .fillMaxWidth()
                .height(35.dp)
                .clip(shape)
                .background(Color.White)
                .border(
                    width = 1.5.dp,
                    color = Blue500,
                    shape = shape
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun GameScreenPrev() {
    Prev() {
        GameScreen()
    }
}