package com.magictools.commandsBox.commonpresentation.utils

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

internal actual fun createViewModelSavedStateHandle(extras: CreationExtras): SavedStateHandle =
    extras.createSavedStateHandle()
