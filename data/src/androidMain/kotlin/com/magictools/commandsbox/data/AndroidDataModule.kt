package com.magictools.commandsbox.data

import android.content.Context
import androidx.room.Room
import com.magictools.commandsbox.data.internal.db.AppDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
@ContributesTo(AppScope::class)
object AndroidDataBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabaseBuilder(context: Context): DatabaseBuilder {
        val dbFile = context.getDatabasePath("commandsbox.db")
        return DatabaseBuilder(
            Room.databaseBuilder<AppDatabase>(
                context = context,
                name = dbFile.absolutePath,
            )
        )
    }
}
