package ir.unalzo.navigation.routes

sealed interface RootRoute : Route {
    data object Welcome : RootRoute
    data object Introduction : RootRoute
    data object Questions : RootRoute
    data object Main : RootRoute
    data object NewTask : RootRoute
    data object LockGame : RootRoute
    data object OrientationGame : RootRoute
    data object ImageGuessGame : RootRoute
    data object TwoByTwoGame : RootRoute
}