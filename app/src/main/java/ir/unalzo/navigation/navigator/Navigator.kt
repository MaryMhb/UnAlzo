package ir.unalzo.navigation.navigator

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


abstract class Navigator<T: NavKey>(defaultValue:T){
    protected val _backstack = MutableStateFlow<List<T>>(listOf(defaultValue))
    val backstack = _backstack.asStateFlow()

    open fun navigate(route:T){
        if(confirmNavigation(route)){
            _backstack.update { it+route }
        }
    }
    fun confirmNavigation(route: T): Boolean{
        return _backstack.value.lastOrNull() != route
    }

    fun navigateUp(){
        _backstack.update { it.toMutableList().apply { removeLastOrNull() } }
    }
    fun navigateAndClear(route:T){
        _backstack.update { listOf(route) }
    }
    fun navigate(route:T,backTo:T){
        if(confirmNavigation(route)){
            _backstack.update { listOf(backTo,route) }
        }
    }
}