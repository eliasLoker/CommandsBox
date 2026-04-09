package com.magictools.multimvi.action

interface ViewAction

class ViewActionBundle internal constructor(
    private val actions: List<ViewAction> = emptyList()
) : List<ViewAction> by actions

fun List<ViewAction>.toViewActionBundle() = ViewActionBundle(this)

sealed interface DefaultViewAction : ViewAction {
    data object Dismiss : DefaultViewAction
}