package com.magictools.commandsBox.commonpresentation.ui.home

import com.magictools.commandsBox.commonpresentation.model.AddBannerViewState
import com.magictools.commandsBox.commonpresentation.model.CommandState
import com.magictools.multimvi.state.ViewState
import kotlinx.serialization.Serializable

@Serializable
data class HomeViewState(
    val tabs: List<HomeTab> = emptyList(),
    val addBannerViewState: AddBannerViewState = AddBannerViewState(),
    val commands: List<CommandState> = emptyList(),
    val selectedTabId: Long = -1,
    val deleteCommandDialogState: DeleteCommandDialogState = DeleteCommandDialogState(),
    val editTabDialogState: EditTabDialogState = EditTabDialogState(),
    val deleteTabDialogState: DeleteTabDialogState = DeleteTabDialogState()
): ViewState {
    @Serializable
    data class HomeTab(
        val id: Long,
        val name: String,
    )

    @Serializable
    data class DeleteCommandDialogState(
        val isVisible: Boolean = false,
        val title: String = "",
        val subtitle: String = "",
        val confirmButton: String = "",
        val dismissButton: String = ""
    )
    @Serializable
    data class EditTabDialogState(
        val isVisible: Boolean = false,
        val title: String = "",
        val initialName: String = "",
        val confirmButton: String = "",
        val dismissButton: String = ""
    )
    @Serializable
    data class DeleteTabDialogState(
        val isVisible: Boolean = false,
        val title: String = "",
        val subtitle: String = "",
        val confirmButton: String = "",
        val dismissButton: String = ""
    )
}
