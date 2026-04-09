package com.magictools.commandsbox.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TabModel(
    val id: Long,
    val name: String,
    val orderIndex: Int
)

@Serializable
data class CommandModel(
    val id: Long,
    val tabId: Long,
    val text: String,
    val description: String,
    val orderIndex: Int
)

@Serializable
data class TabWithCommandsModel(
    val tab: TabModel,
    val commands: List<CommandModel>
)