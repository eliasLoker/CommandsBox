package com.magictools.commandsbox.data

import androidx.room.RoomDatabase
import com.magictools.commandsbox.data.internal.db.AppDatabase

class DatabaseBuilder internal constructor(
    internal val roomBuilder: RoomDatabase.Builder<AppDatabase>
)
