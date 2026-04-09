package com.magictools.commandsbox.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.magictools.commandsbox.data.internal.db.AppDatabase
import org.koin.dsl.module

val androidDataModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val context: Context = get()
        val dbFile = context.getDatabasePath("commandsbox.db")
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath,
        )
    }
}