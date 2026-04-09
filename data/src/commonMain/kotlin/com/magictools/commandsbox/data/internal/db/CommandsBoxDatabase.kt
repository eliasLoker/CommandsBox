package com.magictools.commandsbox.data.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

@Database(entities = [TabEntity::class, CommandEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun companionsDao(): CommandsDao
}

internal fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}