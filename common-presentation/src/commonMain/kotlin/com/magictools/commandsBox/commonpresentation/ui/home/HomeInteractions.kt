package com.magictools.commandsBox.commonpresentation.ui.home

import com.magictools.multimvi.action.ViewAction
import com.magictools.multimvi.event.ViewEvent

sealed interface HomeAction : ViewAction

sealed interface HomeEvent : ViewEvent
class OnTabSelected(val tabId: Long) : HomeEvent
data object OnAddBannerExpandIconClicked : HomeEvent
data object OnAddTabClicked : HomeEvent
data object OnAddCommandClicked : HomeEvent
class OnTabQueryChange(val text: String) : HomeEvent
class OnCommandQueryChange(val text: String) : HomeEvent
class OnDescriptionQueryChange(val text: String) : HomeEvent

class OnEditCommandQueryChange(val id: Long, val text: String) : HomeEvent
class OnEditDescriptionQueryChange(val id: Long, val text: String) : HomeEvent
class OnEditCloseClicked(val id: Long) : HomeEvent
class OnEditApplyClicked(val id: Long) : HomeEvent
class OnCopyCommandClicked(val id: Long) : HomeEvent
class OnCommandUpClicked(val id: Long) : HomeEvent
class OnCommandDownClicked(val id: Long) : HomeEvent
class OnEnableEditClicked(val id: Long) : HomeEvent
class OnDeleteCommandClicked(val id: Long) : HomeEvent
data object OnDeleteCommandDialogDismiss : HomeEvent
data object OnDeleteCommandDialogConfirm : HomeEvent
class OnTabLeftClicked(val id: Long) : HomeEvent
class OnTabRightClicked(val id: Long) : HomeEvent
class OnTabEditClicked(val id: Long) : HomeEvent
data object OnTabEditDialogDismiss : HomeEvent
data object OnTabEditDialogConfirm : HomeEvent
class OnTabEditDialogTextChanged(val text: String) : HomeEvent
class OnDeleteTabClicked(val id: Long) : HomeEvent
data object OnDeleteTabDialogDismiss : HomeEvent
data object OnDeleteTabDialogConfirm : HomeEvent
