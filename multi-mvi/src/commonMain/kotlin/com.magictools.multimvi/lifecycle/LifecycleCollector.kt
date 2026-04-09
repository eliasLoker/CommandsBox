package com.magictools.multimvi.lifecycle

interface LifecycleCollector {

    fun onResume() = Unit

    fun onStart() = Unit

    fun onPause() = Unit

    fun onStop() = Unit
}