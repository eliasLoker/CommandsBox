package com.magictools.commandsbox.data

import androidx.room.Room
import com.magictools.commandsbox.data.internal.db.AppDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
@BindingContainer
@ContributesTo(AppScope::class)
object IosDataBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabaseBuilder(): DatabaseBuilder {
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
        return DatabaseBuilder(
            Room.databaseBuilder<AppDatabase>(
                name = dbPath
            )
        )
    }
}
