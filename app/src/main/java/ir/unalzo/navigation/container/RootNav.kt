package ir.unalzo.navigation.container

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.navigation.routes.RootRoute
import ir.unalzo.ui.screens.IntroScreen
import ir.unalzo.ui.screens.MainScreen
import ir.unalzo.ui.screens.WelcomeScreen
import ir.unalzo.ui.screens.games.imageGuess.ImageGuessScreen
import ir.unalzo.ui.screens.games.lock.LockGameScreen
import ir.unalzo.ui.screens.games.orientation.OrientationGameScreen
import ir.unalzo.ui.screens.games.twoByTwo.TwoByTwoGameScreen
import ir.unalzo.ui.screens.newTask.NewTaskScreen
import ir.unalzo.ui.screens.questions.QuestionsScreen
import org.koin.compose.koinInject

@Composable
fun RootNav(
    navigator: RootNavigator = koinInject()
) {
    NavContainer(
        modifier= Modifier,
        navigator = navigator
    ) {
        when(val route = it){
            RootRoute.Introduction -> NavEntry(route){
                IntroScreen()
            }
            RootRoute.Main -> NavEntry(route){
                MainScreen()
            }
            RootRoute.NewTask -> NavEntry(route){
                NewTaskScreen()
            }
            RootRoute.Questions -> NavEntry(route){
                QuestionsScreen()
            }
            RootRoute.Welcome -> NavEntry(route){
                WelcomeScreen()
            }
            RootRoute.OrientationGame -> NavEntry(it) {
                OrientationGameScreen()
            }

            RootRoute.ImageGuessGame -> NavEntry(it) {
                ImageGuessScreen()
            }

            RootRoute.LockGame -> NavEntry(it) {
                LockGameScreen()
            }

            RootRoute.TwoByTwoGame -> NavEntry(it) {
                TwoByTwoGameScreen()
            }
        }
    }
}