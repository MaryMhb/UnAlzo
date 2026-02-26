package ir.unalzo.navigation.routes

import ir.unalzo.R

sealed interface BottomBarRoute : Route {

    // used to detect which item is currently selected
    val bottomBarKey: String

    sealed interface MainRoute : BottomBarRoute {
        val selectedIcon: Int
        val unselectedIcon: Int

        data object Home : MainRoute {
            override val selectedIcon: Int = R.drawable.ic_home_selected
            override val unselectedIcon: Int = R.drawable.ic_home_unselected

            override val bottomBarKey: String = "home"

        }

        data object Game : MainRoute {
            override val selectedIcon: Int = R.drawable.ic_game_pad_selected
            override val unselectedIcon: Int = R.drawable.ic_game_pad_unselected

            override val bottomBarKey: String = "game"
        }

        data object Tasks : MainRoute {
            override val selectedIcon: Int = R.drawable.ic_calendar_selected
            override val unselectedIcon: Int = R.drawable.ic_calendar_unselected

            override val bottomBarKey: String = "tasks"
        }
    }
}