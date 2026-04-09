package com.magictools.commandsbox.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.magictools.commandsbox.data.internal.db.AppDatabase
import org.koin.dsl.module
import java.io.File

val desktopDataModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val homePath = System.getProperty("user.home")
        val dbFile = File(homePath, ".commandsbox/commandsbox.db")
        if (!dbFile.parentFile.exists()) {
            dbFile.parentFile.mkdirs()
        }
        Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath
        )
    }
}