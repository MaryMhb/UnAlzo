package ir.unalzo.ui.screens.games.orientation

import ir.unalzo.R
import ir.unalzo.base.AnimatedContentState

data class OrientationGameState(
    val orientations: List<ShowOrientation> = emptyList(),
    val currentVisibleOrientationCharacter: ShowOrientation? = null,
    val shouldPickNextOrientation: Boolean = false,
    val scene: Scene = Scene.Beginning,
    val selectedOrientationByUser: GameOrientation? = null,
    val userPickLevel: Int? = null,
    val gameLevel: Int = 1,
) {
    sealed class Scene : AnimatedContentState {
        data object Beginning : Scene() {
            override val key: String
                get() = "beginning"
        }

        data object Show : Scene() {
            override val key: String
                get() = "show"
        }

        data object YourTurn : Scene() {
            override val key: String
                get() = "turn"
        }

        data object Pick : Scene() {
            override val key: String
                get() = "pick"
        }

        data class Result(val correct: Boolean) : Scene() {
            override val key: String
                get() = "result"
        }
    }

    enum class GameOrientation(val character: Int, val arrow: Int) {
        Top(R.drawable.orientation_top, R.drawable.arrow_top),
        Bottom(R.drawable.orientation_bottom, R.drawable.arrow_bottom),
        Left(R.drawable.orientation_left, R.drawable.arrow_left),
        Right(R.drawable.orientation_right, R.drawable.arrow_right)
    }

    data class ShowOrientation(
        val id: Int,
        val orientation: GameOrientation,
    )

}