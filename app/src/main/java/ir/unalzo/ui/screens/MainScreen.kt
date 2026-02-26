package ir.unalzo.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ir.unalzo.navigation.container.BottomBarNav
import ir.unalzo.navigation.navigator.BottomBarNavigator
import ir.unalzo.ui.components.BottomBar
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    bottomBarNavigator: BottomBarNavigator = koinInject()
) {

    val bottomBarStack by bottomBarNavigator.backstack.collectAsState()

    Scaffold(
        modifier=Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                selectedItemKey = bottomBarStack.last().bottomBarKey,
                onSelect = bottomBarNavigator::navigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding->
        BottomBarNav(
            modifier=Modifier.padding(innerPadding)
        )
    }
}