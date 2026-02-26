package ir.unalzo.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.unalzo.navigation.routes.BottomBarRoute
import ir.unalzo.ui.theme.Prev

@Composable
fun BottomBar(
    selectedItemKey: String,
    onSelect: (BottomBarRoute.MainRoute) -> Unit
) {

    val items = listOf(
        BottomBarRoute.MainRoute.Game,
        BottomBarRoute.MainRoute.Home,
        BottomBarRoute.MainRoute.Tasks,
    )
    Row(modifier = Modifier.fillMaxWidth()) {
        items.forEach {
            Box(Modifier
                .weight(1f)
                .clickable {
                    onSelect(it)
                }
                .padding(vertical = 16.dp)
                .navigationBarsPadding()
                , contentAlignment = Alignment.Center) {
                AnimatedContent(
                    targetState = it.bottomBarKey == selectedItemKey
                ) { isSelected ->
                    Image(
                        painter = painterResource(if (isSelected) it.selectedIcon else it.unselectedIcon),
                        modifier = Modifier.size(40.dp),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    Prev {
        BottomBar(
            selectedItemKey = "home",
            onSelect = {

            }
        )
    }
}