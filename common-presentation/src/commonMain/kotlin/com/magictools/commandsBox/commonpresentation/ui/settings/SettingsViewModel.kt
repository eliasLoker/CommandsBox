package com.magictools.commandsBox.commonpresentation.ui.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.magictools.commandsBox.commonpresentation.utils.AppMainDispatcher
import com.magictools.commandsBox.commonpresentation.utils.createViewModelSavedStateHandle
import dev.zacsweers.metro.AppScope
import com.magictools.commandsbox.data.ThemePreference
import com.magictools.commandsbox.data.ThemePreferences
import com.magictools.multimvi.action.actions
import com.magictools.multimvi.event.listen
import com.magictools.multimvi.state.viewState
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.launch

@AssistedInject
class SettingsViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
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

    @AssistedFactory
    @ViewModelAssistedFactoryKey(SettingsViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): SettingsViewModel =
            create(createViewModelSavedStateHandle(extras))

        fun create(@Assisted savedStateHandle: SavedStateHandle): SettingsViewModel
    }
}
