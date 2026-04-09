package com.magictools.commandsBox.commonui.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magictools.commandsBox.commonui.customview.AddBannerViewState
import com.magictools.commandsBox.commonui.customview.CommandState
import com.magictools.commandsBox.commonui.customview.InputViewState
import com.magictools.commandsBox.commonui.customview.ToastState
import com.magictools.commandsBox.commonui.ui.home.HomeViewState.DeleteCommandDialogState
import com.magictools.commandsBox.commonui.ui.home.HomeViewState.EditTabDialogState
import com.magictools.commandsBox.commonui.ui.main.MainToastEvents
import com.magictools.commandsBox.commonui.utils.AppMainDispatcher
import com.magictools.commandsBox.commonui.utils.CustomClipboardManager
import com.magictools.commandsbox.data.MainRepository
import com.magictools.commandsbox.data.model.TabWithCommandsModel
import com.magictools.commandsbox.resources.Res
import com.magictools.commandsbox.resources.add_command_hint
import com.magictools.commandsbox.resources.add_description_hint
import com.magictools.commandsbox.resources.add_tab_name_hint
import com.magictools.commandsbox.resources.cancel
import com.magictools.commandsbox.resources.command_copy
import com.magictools.commandsbox.resources.commands_delete_subtitle
import com.magictools.commandsbox.resources.commands_delete_title
import com.magictools.commandsbox.resources.common_error
import com.magictools.commandsbox.resources.delete
import com.magictools.commandsbox.resources.edit
import com.magictools.commandsbox.resources.tab_delete_subtitle
import com.magictools.commandsbox.resources.tab_delete_title
import com.magictools.commandsbox.resources.tab_edit_title
import com.magictools.composeui.main.utils.StringProvider
import com.magictools.multimvi.action.actions
import com.magictools.multimvi.event.listen
import com.magictools.multimvi.state.viewState
import com.magictools.multimvi.viewmodelstate.state
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

internal class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    private val stringProvider: StringProvider,
    private val mainRepository: MainRepository,
    private val customClipboardManager: CustomClipboardManager,
    private val mainToastEvents: MainToastEvents
) : ViewModel() {

    private var state by savedStateHandle.state(initialValue = State())

    val viewState by savedStateHandle.viewState(HomeViewState(), HomeViewState.serializer())

    val action by actions()

    val onNewEvent by listen<HomeEvent> { event ->
        when (event) {
            is OnTabSelected -> {
                val selectedTabName = state.initialTabs
                    .find { it.tab.id == event.tabId }?.tab?.name ?: ""
                val commands = state.initialTabs.find { it.tab.id == event.tabId }?.commands?.map {
                    CommandState(
                        id = it.id,
                        isEditMode = false,
                        command = it.text,
                        description = it.description
                    )
                } ?: emptyList()
                viewState.value = viewState.value.copy(
                    selectedTabId = event.tabId,
                    addBannerViewState = viewState.value.addBannerViewState.copy(
                        selectedTabName = selectedTabName
                    ),
                    commands = commands
                )
            }

            OnAddBannerExpandIconClicked -> viewState.value = viewState.value.copy(
                addBannerViewState = viewState.value.addBannerViewState.copy(
                    isExpanded = !viewState.value.addBannerViewState.isExpanded
                )
            )

            is OnTabQueryChange -> viewState.value = viewState.value.copy(
                addBannerViewState = viewState.value.addBannerViewState.copy(
                    tabInputViewState = viewState.value.addBannerViewState.tabInputViewState.copy(
                        query = event.text
                    )
                )
            )

            OnAddTabClicked -> viewModelScope.launch(AppMainDispatcher) {
                val insertResult =
                    mainRepository.insertTab(viewState.value.addBannerViewState.tabInputViewState.query)
                insertResult.onFailure { showFailureToast() }
            }

            is OnCommandQueryChange -> viewState.value = viewState.value.copy(
                addBannerViewState = viewState.value.addBannerViewState.copy(
                    commandInputViewState = viewState.value.addBannerViewState.commandInputViewState.copy(
                        query = event.text
                    )
                )
            )

            is OnDescriptionQueryChange -> viewState.value = viewState.value.copy(
                addBannerViewState = viewState.value.addBannerViewState.copy(
                    descriptionInputViewState = viewState.value.addBannerViewState.descriptionInputViewState.copy(
                        query = event.text
                    )
                )
            )

            OnAddCommandClicked -> viewModelScope.launch(AppMainDispatcher) {
                val insertResult = mainRepository.insertCommand(
                    tabId = viewState.value.selectedTabId,
                    command = viewState.value.addBannerViewState.commandInputViewState.query,
                    description = viewState.value.addBannerViewState.descriptionInputViewState.query
                )
                insertResult.onFailure { showFailureToast() }
            }

            is OnEnableEditClicked -> viewModelScope.launch(AppMainDispatcher) {
                val commandItem =
                    state.initialTabs.find { it.tab.id == viewState.value.selectedTabId }?.commands?.find { it.id == event.id }
                        ?: return@launch
                val command = commandItem.text
                val description = commandItem.description
                viewState.value = viewState.value.copy(
                    commands = viewState.value.commands.map {
                        if (it.id == event.id) {
                            it.copy(
                                isEditMode = true,
                                commandInputViewState = InputViewState(
                                    query = command,
                                    hint = stringProvider.getString(Res.string.add_command_hint),
                                    icon = InputViewState.InputIcon.COMMAND,
                                    maxLines = 5,
                                    singleLine = false
                                ),
                                descriptionInputViewState = InputViewState(
                                    query = description,
                                    hint = stringProvider.getString(Res.string.add_description_hint),
                                    icon = InputViewState.InputIcon.DESCRIPTION,
                                    maxLines = 5,
                                    singleLine = false
                                ),
                            )
                        } else {
                            it
                        }
                    }
                )
            }

            is OnEditCloseClicked -> viewState.value = viewState.value.copy(
                commands = viewState.value.commands.map {
                    if (it.id == event.id) {
                        it.copy(isEditMode = false)
                    } else {
                        it
                    }
                }
            )

            is OnEditCommandQueryChange -> {
                viewState.value = viewState.value.copy(
                    commands = viewState.value.commands.map {
                        if (it.id == event.id) {
                            it.copy(
                                commandInputViewState = it.commandInputViewState.copy(
                                    query = event.text,
                                ),
                            )
                        } else {
                            it
                        }
                    }
                )
            }

            is OnEditDescriptionQueryChange -> {
                viewState.value = viewState.value.copy(
                    commands = viewState.value.commands.map {
                        if (it.id == event.id) {
                            it.copy(
                                descriptionInputViewState = it.descriptionInputViewState.copy(
                                    query = event.text,
                                ),
                            )
                        } else {
                            it
                        }
                    }
                )
            }

            is OnEditApplyClicked -> viewModelScope.launch(AppMainDispatcher) {
                val command = viewState.value.commands.find { it.id == event.id } ?: return@launch
                val newCommand = command.commandInputViewState.query
                val newDescription = command.descriptionInputViewState.query
                val updateResult = mainRepository.updateCommand(
                    id = command.id,
                    command = newCommand,
                    description = newDescription
                )
                updateResult.onFailure { showFailureToast() }
            }

            is OnCommandDownClicked -> viewModelScope.launch(AppMainDispatcher) {
                val moveResult = mainRepository.moveCommandDown(event.id)
                moveResult.onFailure { showFailureToast() }
            }

            is OnCommandUpClicked -> viewModelScope.launch(AppMainDispatcher) {
                val moveResult = mainRepository.moveCommandUp(event.id)
                moveResult.onFailure { showFailureToast() }
            }

            is OnDeleteCommandClicked -> viewModelScope.launch(AppMainDispatcher) {
                val command = viewState.value.commands.find { it.id == event.id } ?: return@launch
                state = state.copy(commandForDeletionId = command.id)
                viewState.value = viewState.value.copy(
                    deleteCommandDialogState = DeleteCommandDialogState(
                        isVisible = true,
                        title = stringProvider.getString(Res.string.commands_delete_title),
                        subtitle = stringProvider.getString(
                            Res.string.commands_delete_subtitle,
                            command.command.take(15)
                        ),
                        confirmButton = stringProvider.getString(Res.string.delete),
                        dismissButton = stringProvider.getString(Res.string.cancel),
                    )
                )
            }

            OnDeleteCommandDialogConfirm -> viewModelScope.launch(AppMainDispatcher) {
                val deleteResult = mainRepository.deleteCommand(state.commandForDeletionId)
                deleteResult.onSuccess {
                    state = state.copy(commandForDeletionId = -1L)
                    viewState.value = viewState.value.copy(
                        deleteCommandDialogState = viewState.value.deleteCommandDialogState.copy(
                            isVisible = false
                        )
                    )
                }
                deleteResult.onFailure {
                    state = state.copy(commandForDeletionId = -1L)
                    viewState.value = viewState.value.copy(
                        deleteCommandDialogState = viewState.value.deleteCommandDialogState.copy(
                            isVisible = false
                        )
                    )
                    showFailureToast()
                }
            }

            OnDeleteCommandDialogDismiss -> {
                state = state.copy(commandForDeletionId = -1L)
                viewState.value = viewState.value.copy(
                    deleteCommandDialogState = viewState.value.deleteCommandDialogState.copy(
                        isVisible = false
                    )
                )
            }

            is OnTabLeftClicked -> viewModelScope.launch(AppMainDispatcher) {
                val moveResult = mainRepository.moveTabLeft(event.id)
                moveResult.onFailure { showFailureToast() }
            }

            is OnTabRightClicked -> viewModelScope.launch(AppMainDispatcher) {
                val moveResult = mainRepository.moveTabRight(event.id)
                moveResult.onFailure { showFailureToast() }
            }

            is OnTabEditClicked -> viewModelScope.launch(AppMainDispatcher) {
                val tab =
                    state.initialTabs.find { it.tab.id == event.id } ?: return@launch
                state = state.copy(tabForEditId = tab.tab.id)
                viewState.value = viewState.value.copy(
                    editTabDialogState = EditTabDialogState(
                        isVisible = true,
                        title = stringProvider.getString(Res.string.tab_edit_title),
                        initialName = tab.tab.name,
                        confirmButton = stringProvider.getString(Res.string.edit),
                        dismissButton = stringProvider.getString(Res.string.cancel)
                    )
                )
            }

            is OnTabEditDialogTextChanged -> viewState.value = viewState.value.copy(
                editTabDialogState = viewState.value.editTabDialogState.copy(
                    initialName = event.text
                )
            )

            OnTabEditDialogConfirm -> viewModelScope.launch(AppMainDispatcher) {
                val renameResult = mainRepository.renameTab(
                    state.tabForEditId,
                    viewState.value.editTabDialogState.initialName
                )
                renameResult.onSuccess {
                    state = state.copy(tabForEditId = -1)
                    viewState.value = viewState.value.copy(
                        editTabDialogState = viewState.value.editTabDialogState.copy(
                            isVisible = false
                        )
                    )
                }
                renameResult.onFailure {
                    state = state.copy(tabForEditId = -1)
                    viewState.value = viewState.value.copy(
                        editTabDialogState = viewState.value.editTabDialogState.copy(
                            isVisible = false
                        )
                    )
                    showFailureToast()
                }
            }

            OnTabEditDialogDismiss -> {
                state = state.copy(tabForEditId = -1)
                viewState.value = viewState.value.copy(
                    editTabDialogState = viewState.value.editTabDialogState.copy(
                        isVisible = false
                    )
                )
            }

            is OnDeleteTabClicked -> viewModelScope.launch(AppMainDispatcher) {
                val tab =
                    state.initialTabs.find { it.tab.id == event.id } ?: return@launch
                state = state.copy(tabForDeletionId = tab.tab.id)
                viewState.value = viewState.value.copy(
                    deleteTabDialogState = HomeViewState.DeleteTabDialogState(
                        isVisible = true,
                        title = stringProvider.getString(Res.string.tab_delete_title),
                        subtitle = stringProvider.getString(
                            Res.string.tab_delete_subtitle,
                            tab.tab.name
                        ),
                        confirmButton = stringProvider.getString(Res.string.delete),
                        dismissButton = stringProvider.getString(Res.string.cancel),
                    )
                )
            }

            OnDeleteTabDialogDismiss -> {
                state = state.copy(tabForDeletionId = -1)
                viewState.value = viewState.value.copy(
                    deleteTabDialogState = viewState.value.deleteTabDialogState.copy(
                        isVisible = false
                    )
                )
            }

            OnDeleteTabDialogConfirm -> viewModelScope.launch(AppMainDispatcher) {
                val deleteResult = mainRepository.deleteTab(state.tabForDeletionId)
                deleteResult.onSuccess {
                    state = state.copy(tabForDeletionId = -1L)
                    viewState.value = viewState.value.copy(
                        deleteTabDialogState = viewState.value.deleteTabDialogState.copy(
                            isVisible = false
                        )
                    )
                }
                deleteResult.onFailure {
                    state = state.copy(tabForDeletionId = -1L)
                    viewState.value = viewState.value.copy(
                        deleteTabDialogState = viewState.value.deleteTabDialogState.copy(
                            isVisible = false
                        )
                    )
                    showFailureToast()
                }
            }

            is OnCopyCommandClicked -> viewModelScope.launch(AppMainDispatcher) {
                val command = viewState.value.commands.find { it.id == event.id } ?: return@launch
                customClipboardManager.copy(command.command)
                mainToastEvents.emit(
                    state = ToastState.Data(
                        type = ToastState.Type.SUCCESS,
                        message = stringProvider.getString(Res.string.command_copy)
                    )
                )
            }
        }
    }

    init {
        viewModelScope.launch(AppMainDispatcher) {
            mainRepository.subscribeToTabs()
                .catch { showFailureToast() }
                .collect { tabsWithCommands ->
                    state = state.copy(initialTabs = tabsWithCommands)
                    val selectedTabId = when {
                        tabsWithCommands.isEmpty() -> -1L
                        else -> when {
                            viewState.value.selectedTabId != -1L -> viewState.value.selectedTabId
                            else -> tabsWithCommands.first().tab.id
                        }
                    }
                    val selectedTabName = when {
                        selectedTabId == -1L -> ""
                        else -> tabsWithCommands.find { it.tab.id == selectedTabId }?.tab?.name
                            ?: ""
                    }
                    viewState.value = viewState.value.copy(
                        selectedTabId = selectedTabId,
                        tabs = tabsWithCommands.map {
                            HomeViewState.HomeTab(it.tab.id, it.tab.name)
                        },
                        addBannerViewState = AddBannerViewState(
                            isExpanded = viewState.value.addBannerViewState.isExpanded,
                            hasTabs = tabsWithCommands.isNotEmpty(),
                            selectedTabName = selectedTabName,
                            tabInputViewState = InputViewState(
                                query = "",
                                hint = stringProvider.getString(Res.string.add_tab_name_hint),
                                maxLines = 1,
                                singleLine = true,
                                maxLength = MAX_LENGTH_TAB_NAME,
                                icon = InputViewState.InputIcon.TAB
                            ),
                            commandInputViewState = InputViewState(
                                query = "",
                                hint = stringProvider.getString(Res.string.add_command_hint),
                                maxLines = 1,
                                singleLine = true,
                                maxLength = MAX_LENGTH_COMMANDS_FIELDS,
                                icon = InputViewState.InputIcon.COMMAND
                            ),
                            descriptionInputViewState = InputViewState(
                                query = "",
                                hint = stringProvider.getString(Res.string.add_description_hint),
                                maxLines = 3,
                                singleLine = false,
                                maxLength = MAX_LENGTH_COMMANDS_FIELDS,
                                icon = InputViewState.InputIcon.DESCRIPTION
                            )
                        ),
                        commands = tabsWithCommands.find { it.tab.id == selectedTabId }?.commands?.map {
                            CommandState(
                                id = it.id,
                                isEditMode = false,
                                command = it.text,
                                description = it.description
                            )
                        } ?: emptyList()
                    )
                }
        }
    }

    private fun showFailureToast() {
        viewModelScope.launch(AppMainDispatcher) {
            mainToastEvents.emit(
                state = ToastState.Data(
                    type = ToastState.Type.ERROR,
                    message = stringProvider.getString(Res.string.common_error)
                )
            )
        }
    }

    @Serializable
    private data class State(
        val initialTabs: List<TabWithCommandsModel> = emptyList(),
        val commandForDeletionId: Long = -1L,
        val tabForDeletionId: Long = -1L,
        val tabForEditId: Long = -1,
    )

    private companion object {

        const val MAX_LENGTH_COMMANDS_FIELDS = 2000
        const val MAX_LENGTH_TAB_NAME = 50
    }
}