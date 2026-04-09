package com.magictools.multimvi.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun LifecycleCollector.collectLifecycle(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) = this@collectLifecycle.onPause()
        override fun onResume(owner: LifecycleOwner) = this@collectLifecycle.onResume()
        override fun onStart(owner: LifecycleOwner) = this@collectLifecycle.onStart()
        override fun onStop(owner: LifecycleOwner) = this@collectLifecycle.onStop()
        override fun onDestroy(owner: LifecycleOwner) = lifecycleOwner.lifecycle.removeObserver(this)
    })
}