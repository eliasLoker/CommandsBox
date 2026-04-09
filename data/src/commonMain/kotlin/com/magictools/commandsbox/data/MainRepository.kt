package com.magictools.commandsbox.data

import com.magictools.commandsbox.data.model.TabWithCommandsModel
import kotlinx.coroutines.flow.Flow

interface MainRepository {

//    suspend fun getTabs(): Int

    suspend fun insertTab(name: String): Result<Unit>

    suspend fun subscribeToTabs(): Flow<List<TabWithCommandsModel>>

    suspend fun insertCommand(tabId: Long, command: String, description: String): Result<Unit>

    suspend fun updateCommand(id: Long, command: String, description: String): Result<Unit>

    suspend fun moveCommandDown(id: Long): Result<Unit>

    suspend fun moveCommandUp(id: Long): Result<Unit>

    suspend fun moveTabLeft(id: Long): Result<Unit>

    suspend fun moveTabRight(id: Long): Result<Unit>

    suspend fun deleteCommand(id: Long): Result<Unit>

    suspend fun renameTab(id: Long, newName: String): Result<Unit>

    suspend fun deleteTab(id: Long): Result<Unit>
}