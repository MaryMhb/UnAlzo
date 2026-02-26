package ir.unalzo.navigation.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.unalzo.navigation.navigator.Navigator
import ir.unalzo.navigation.routes.Route

@Composable
fun <T: Route>NavContainer(
    modifier: Modifier,
    navigator: Navigator<T>,
    entryProvider: (key: T) -> NavEntry<T>
) {
    val backstack by navigator.backstack.collectAsState()
    NavDisplay(
        entryProvider = entryProvider,
        onBack = navigator::navigateUp,
        backStack = backstack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
    )
}