package com.magictools.commandsBox.commonpresentation.ui.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magictools.commandsBox.commonpresentation.utils.AppMainDispatcher
import com.magictools.commandsbox.data.ThemePreference
import com.magictools.commandsbox.data.ThemePreferences
import com.magictools.multimvi.action.actions
import com.magictools.multimvi.event.listen
import com.magictools.multimvi.state.viewState
import kotlinx.coroutines.launch

class SettingsViewModel(
    savedStateHandle: SavedStateHandle,
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val viewState by savedStateHandle.viewState(
        SettingsViewState(selectedTheme = themePreferences.selectedTheme.value),
        SettingsViewState.serializer()
    )

    val action by actions()

    val onNewEvent by listen<SettingsEvent> { event ->
        when (event) {
            OnBackClicked -> action.emit(SettingsAction.NavigateBack)

            is OnThemeChanged -> {
                val theme = when {
                    event.isDark -> ThemePreference.DARK
                    else -> ThemePreference.LIGHT
                }
                themePreferences.setTheme(theme)
                viewState.value = viewState.value.copy(selectedTheme = theme)
            }
        }
    }

    init {
        viewModelScope.launch(AppMainDispatcher) {
            themePreferences.selectedTheme.collect { selectedTheme ->
                viewState.value = viewState.value.copy(selectedTheme = selectedTheme)
            }
        }
    }
}
