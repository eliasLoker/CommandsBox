package com.magictools.commandsBox.commonpresentation.utils

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

internal expect fun createViewModelSavedStateHandle(extras: CreationExtras): SavedStateHandle
