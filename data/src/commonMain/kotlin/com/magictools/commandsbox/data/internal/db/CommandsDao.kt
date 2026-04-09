package com.magictools.commandsbox.data.internal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CommandsDao {

    @Query("SELECT COUNT(*) FROM tabs")
    suspend fun getTabsCount(): Int

    @Transaction
    suspend fun insertTab(name: String): Long {
        val nextIndex = getTabsCount()
        val newTab = TabEntity(
            name = name,
            orderIndex = nextIndex
        )
        return insertInternal(newTab)
    }

    @Transaction
    @Query("SELECT * FROM tabs ORDER BY order_index ASC")
    fun getTabsWithCommandsFlow(): Flow<List<TabWithCommandsEntity>>

    @Transaction
    suspend fun insertCommandAtEnd(tabId: Long, text: String, description: String): Long {
        val nextIndex = getCommandsCountInTab(tabId)
        val newCommand = CommandEntity(
            tabId = tabId,
            text = text,
            description = description,
            orderIndex = nextIndex
        )
        return insertCommandInternal(newCommand)
    }

    @Query("UPDATE commands SET text = :newCommand, description = :newDescription WHERE id = :commandId")
    suspend fun updateCommandContent(commandId: Long, newCommand: String, newDescription: String)

    @Transaction
    suspend fun moveCommandDown(commandId: Long) {
        val currentCommand = getCommandById(commandId) ?: return
        val nextCommand = getNextCommand(currentCommand.tabId, currentCommand.orderIndex) ?: return
        val updatedCurrent = currentCommand.copy(orderIndex = nextCommand.orderIndex)
        val updatedNext = nextCommand.copy(orderIndex = currentCommand.orderIndex)
        updateCommands(listOf(updatedCurrent, updatedNext))
    }

    @Transaction
    suspend fun moveCommandUp(commandId: Long) {
        val currentCommand = getCommandById(commandId) ?: return
        val prevCommand = getPreviousCommand(currentCommand.tabId, currentCommand.orderIndex) ?: return
        val updatedCurrent = currentCommand.copy(orderIndex = prevCommand.orderIndex)
        val updatedPrev = prevCommand.copy(orderIndex = currentCommand.orderIndex)
        updateCommands(listOf(updatedCurrent, updatedPrev))
    }

    @Query("SELECT * FROM commands WHERE id = :id")
    suspend fun getCommandById(id: Long): CommandEntity?

    @Query(
        """
        SELECT * FROM commands 
        WHERE tab_id = :tabId AND order_index > :currentOrder 
        ORDER BY order_index ASC LIMIT 1
    """
    )
    suspend fun getNextCommand(tabId: Long, currentOrder: Int): CommandEntity?

    @Query("""
        SELECT * FROM commands 
        WHERE tab_id = :tabId AND order_index < :currentOrder 
        ORDER BY order_index DESC LIMIT 1
    """)
    suspend fun getPreviousCommand(tabId: Long, currentOrder: Int): CommandEntity?

    @Update
    suspend fun updateCommands(commands: List<CommandEntity>)

    @Transaction
    suspend fun moveTabLeft(tabId: Long) {
        val currentTab = getTabById(tabId) ?: return
        val prevTab = getPreviousTab(currentTab.orderIndex) ?: return
        val updatedCurrent = currentTab.copy(orderIndex = prevTab.orderIndex)
        val updatedPrev = prevTab.copy(orderIndex = currentTab.orderIndex)
        updateTabs(listOf(updatedCurrent, updatedPrev))
    }

    @Transaction
    suspend fun moveTabRight(tabId: Long) {
        val currentTab = getTabById(tabId) ?: return
        val nextTab = getNextTab(currentTab.orderIndex) ?: return
        val updatedCurrent = currentTab.copy(orderIndex = nextTab.orderIndex)
        val updatedNext = nextTab.copy(orderIndex = currentTab.orderIndex)
        updateTabs(listOf(updatedCurrent, updatedNext))
    }

    @Transaction
    suspend fun deleteCommandAndShift(commandId: Long) {
        val command = getCommandById(commandId) ?: return
        deleteCommandById(commandId)
        shiftIndicesAfterDeletion(command.tabId, command.orderIndex)
    }

    @Query("UPDATE tabs SET name = :newName WHERE id = :tabId")
    suspend fun updateTabName(tabId: Long, newName: String)

    @Transaction
    suspend fun deleteTabAndShift(tabId: Long) {
        val tab = getTabById(tabId) ?: return
        deleteTabById(tabId)
        shiftTabsIndices(tab.orderIndex)
    }

    //

    @Query("DELETE FROM tabs WHERE id = :id")
    suspend fun deleteTabById(id: Long)

    @Query("UPDATE tabs SET order_index = order_index - 1 WHERE order_index > :deletedIndex")
    suspend fun shiftTabsIndices(deletedIndex: Int)

    @Query("DELETE FROM commands WHERE id = :id")
    suspend fun deleteCommandById(id: Long)

    @Query("""
        UPDATE commands 
        SET order_index = order_index - 1 
        WHERE tab_id = :tabId AND order_index > :deletedIndex
    """)
    suspend fun shiftIndicesAfterDeletion(tabId: Long, deletedIndex: Int)

    @Query("SELECT COUNT(*) FROM commands WHERE tab_id = :tabId")
    suspend fun getCommandsCountInTab(tabId: Long): Int

    @Insert
    suspend fun insertCommandInternal(command: CommandEntity): Long

    @Insert
    suspend fun insertInternal(tab: TabEntity): Long

    @Query("SELECT * FROM tabs WHERE id = :id")
    suspend fun getTabById(id: Long): TabEntity?

    @Query("""
        SELECT * FROM tabs 
        WHERE order_index < :currentOrder 
        ORDER BY order_index DESC LIMIT 1
    """)
    suspend fun getPreviousTab(currentOrder: Int): TabEntity?

    @Update
    suspend fun updateTabs(tabs: List<TabEntity>)

    @Query("""
        SELECT * FROM tabs 
        WHERE order_index > :currentOrder 
        ORDER BY order_index ASC LIMIT 1
    """)
    suspend fun getNextTab(currentOrder: Int): TabEntity?
}