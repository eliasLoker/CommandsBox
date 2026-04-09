package com.magictools.commandsbox.data.internal.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "tabs")
internal data class TabEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "order_index")
    val orderIndex: Int
)

@Entity(
    tableName = "commands",
    foreignKeys = [
        ForeignKey(
            entity = TabEntity::class,
            parentColumns = ["id"],
            childColumns = ["tab_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tab_id")]
)
internal data class CommandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "tab_id")
    val tabId: Long,
    val text: String,
    val description: String,
    @ColumnInfo(name = "order_index")
    val orderIndex: Int
)

internal data class TabWithCommandsEntity(
    @Embedded val tab: TabEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tab_id"
    )
    val commands: List<CommandEntity>
)