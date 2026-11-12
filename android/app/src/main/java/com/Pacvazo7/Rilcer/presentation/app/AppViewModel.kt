package com.Pacvazo7.Rilcer.presentation.app

import androidx.lifecycle.ViewModel
import com.Pacvazo7.Rilcer.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()

    private val current = MutableStateFlow<Screen>(Screen.Splash)

    val screen: StateFlow<Screen> = current.asStateFlow()

    fun openMenu() {
        backStack.clear()
        current.value = Screen.Menu
    }

    fun navigate(target: Screen) {
        val from = current.value
        if (from != Screen.Splash) backStack.addLast(from)
        current.value = target
    }

    fun replace(target: Screen) {
        current.value = target
    }

    fun finishRunWith(target: Screen) {
        backStack.clear()
        backStack.addLast(Screen.Menu)
        current.value = target
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        current.value = previous
        return true
    }

    fun popToMenu() {
        backStack.clear()
        current.value = Screen.Menu
    }
}
