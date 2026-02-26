package ir.unalzo.navigation.navigator

import ir.unalzo.navigation.routes.BottomBarRoute
import kotlinx.coroutines.flow.update

class BottomBarNavigator: Navigator<BottomBarRoute>(BottomBarRoute.MainRoute.Home){
    override fun navigate(route: BottomBarRoute) {
        if(confirmNavigation(route)){
            val stack = _backstack.value.toMutableList()
            // remove repeated stacks
            if (_backstack.value.any { it == route }){
                stack.remove(route)
            }
            stack.add(route)
            _backstack.update { stack }
        }
    }
}