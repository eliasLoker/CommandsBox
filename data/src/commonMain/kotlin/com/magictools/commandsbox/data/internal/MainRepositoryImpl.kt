package com.magictools.commandsbox.data.internal

import com.magictools.commandsbox.data.MainRepository
import com.magictools.commandsbox.data.internal.db.CommandsDao
import com.magictools.commandsbox.data.model.TabWithCommandsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MainRepositoryImpl(
    private val commandsDao: CommandsDao,
    private val mainMapper: MainMapper
): MainRepository {

//    override suspend fun getTabs(): Int {
//        return commandsDao.getTabsCount()
//    }

    override suspend fun insertTab(name: String): Result<Unit> = runCatching {
        commandsDao.insertTab(name)
    }

    override suspend fun subscribeToTabs(): Flow<List<TabWithCommandsModel>> {
        return commandsDao.getTabsWithCommandsFlow()
            .map { entities ->
                entities.map { entity ->
                    mainMapper.mapToModel(entity)
                }
            }
    }

    override suspend fun insertCommand(tabId: Long, command: String, description: String): Result<Unit> = runCatching {
        commandsDao.insertCommandAtEnd(tabId, command, description)
    }

    override suspend fun updateCommand(
        id: Long,
        command: String,
        description: String
    ): Result<Unit> = runCatching {
        commandsDao.updateCommandContent(id, command, description)
    }

    override suspend fun moveCommandDown(id: Long): Result<Unit> = runCatching {
        commandsDao.moveCommandDown(id)
    }

    override suspend fun moveCommandUp(id: Long): Result<Unit> = runCatching {
        commandsDao.moveCommandUp(id)
    }

    override suspend fun moveTabLeft(id: Long): Result<Unit> = runCatching {
        commandsDao.moveTabLeft(id)
    }

    override suspend fun moveTabRight(id: Long): Result<Unit> = runCatching {
        commandsDao.moveTabRight(id)
    }

    override suspend fun deleteCommand(id: Long): Result<Unit> = runCatching {
        commandsDao.deleteCommandAndShift(id)
    }

    override suspend fun renameTab(id: Long, newName: String): Result<Unit> = runCatching {
        commandsDao.updateTabName(id, newName)
    }

    override suspend fun deleteTab(id: Long): Result<Unit> = runCatching {
        commandsDao.deleteTabAndShift(id)
    }
}