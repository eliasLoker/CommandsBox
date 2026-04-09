package com.magictools.multimvi.action

import kotlinx.coroutines.flow.Flow

interface ViewActionEmitter : Flow<ViewActionBundle> {

    fun emit(vararg actions: ViewAction)

    suspend fun emitNow(vararg actions: ViewAction)
}