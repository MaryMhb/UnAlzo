package ir.unalzo.navigation.container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import ir.unalzo.R
import ir.unalzo.navigation.navigator.BottomBarNavigator
import ir.unalzo.navigation.routes.BottomBarRoute
import ir.unalzo.ui.screens.main.GameScreen
import ir.unalzo.ui.screens.main.home.HomeScreen
import ir.unalzo.ui.screens.main.tasks.TasksScreen
import ir.unalzo.ui.theme.Blue700
import ir.unalzo.ui.theme.screenPadding
import org.koin.compose.koinInject

@Composable
fun BottomBarNav(
    modifier: Modifier,
    navigator: BottomBarNavigator = koinInject()
) {
    val stack by navigator.backstack.collectAsState()
    val shouldShowProfileIcon = stack.last() is BottomBarRoute.MainRoute
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .padding(screenPadding),
            visible = shouldShowProfileIcon,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Column {
                Icon(
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = null,
                    tint = Blue700,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
        }
        NavContainer(
            modifier = Modifier.fillMaxSize(),
            navigator = navigator
        ) {
            when (val route = it) {


                BottomBarRoute.MainRoute.Game -> NavEntry(it) {
                    GameScreen()
                }

                BottomBarRoute.MainRoute.Home -> NavEntry(it) {
                    HomeScreen()
                }

                BottomBarRoute.MainRoute.Tasks -> NavEntry(it) {
                    TasksScreen()
                }

            }
        }
    }

}