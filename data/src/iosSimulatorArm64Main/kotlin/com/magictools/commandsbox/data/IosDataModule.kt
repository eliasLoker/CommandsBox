package com.magictools.commandsbox.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.magictools.commandsbox.data.internal.db.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
val iosDataModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val appSupportDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSApplicationSupportDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )
        val dbFile = appSupportDirectory?.URLByAppendingPathComponent("commandsbox.db")
            ?: error("Unable to resolve the iOS Application Support directory for the database.")
        val dbPath = dbFile.path
            ?: error("Unable to resolve the iOS database path.")
        Room.databaseBuilder<AppDatabase>(
            name = dbPath
        )
    }
}
