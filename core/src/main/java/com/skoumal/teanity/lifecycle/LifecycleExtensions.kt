package com.skoumal.teanity.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LifecycleCoroutineScope.launchWhenDestroyed(
    lifecycle: Lifecycle,
    body: suspend CoroutineScope.() -> Unit
) {
    launch { lifecycle.whenStateAtLeast(Lifecycle.State.DESTROYED, body) }
}