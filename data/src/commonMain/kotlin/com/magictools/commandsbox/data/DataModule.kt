package com.magictools.commandsbox.data

import com.magictools.commandsbox.data.internal.MainMapper
import com.magictools.commandsbox.data.internal.MainRepositoryImpl
import com.magictools.commandsbox.data.internal.db.AppDatabase
import com.magictools.commandsbox.data.internal.db.getRoomDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {

    single<AppDatabase> { getRoomDatabase(get()) }
    single { get<AppDatabase>().companionsDao() }

    factoryOf(::MainMapper)
    single { MainRepositoryImpl(get(), get()) }.binds(arrayOf(MainRepository::class))
}