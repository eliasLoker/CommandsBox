package com.magictools.commandsbox.data.internal

import com.magictools.commandsbox.data.internal.db.TabWithCommandsEntity
import com.magictools.commandsbox.data.model.CommandModel
import com.magictools.commandsbox.data.model.TabModel
import com.magictools.commandsbox.data.model.TabWithCommandsModel

internal class MainMapper {

    fun mapToModel(entity: TabWithCommandsEntity): TabWithCommandsModel {
        return TabWithCommandsModel(
            tab = TabModel(
                id = entity.tab.id,
                name = entity.tab.name,
                orderIndex = entity.tab.orderIndex
            ),
            commands = entity.commands.map {
                CommandModel(
                    id = it.id,
                    tabId = it.tabId,
                    text = it.text,
                    description = it.description,
                    orderIndex = it.orderIndex
                )
            }.sortedBy { it.orderIndex }
        )
    }
}