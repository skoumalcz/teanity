package com.skoumal.teanity.view

import android.view.View
import androidx.core.view.ViewCompat
import com.skoumal.teanity.extensions.subscribeK
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.viewevent.SimpleViewEvent
import com.skoumal.teanity.viewevent.base.ViewEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber

internal class TeanityDelegate(private val view: TeanityView<*>) {

    private var subscriber: Disposable? = null

    internal inline fun ensureInsets(
        target: View,
        crossinline body: (Insets) -> Unit
    ) = ViewCompat.setOnApplyWindowInsetsListener(target) { _, insets ->
        val left = insets.systemWindowInsetLeft
        val top = insets.systemWindowInsetTop
        val right = insets.systemWindowInsetRight
        val bottom = insets.systemWindowInsetBottom

        view.peekSystemWindowInsets(Insets(left, top, right, bottom))
        val consumedInsets = view.consumeSystemWindowInsets(left, top, right, bottom)?.also(body)

        consumedInsets?.let {
            insets.replaceSystemWindowInsets(
                left - it.left,
                top - it.top,
                right - it.right,
                bottom - it.bottom
            )
        } ?: insets
    }

    internal fun subscribe(events: Observable<ViewEvent>) {
        subscriber = events.subscribeK(onError = { subscribe(events) }) {
            when (it) {
                is SimpleViewEvent -> {
                    Timber.e("SimpleViewEvent is deprecated. See documentation for suggested solution.")
                    view.onSimpleEventDispatched(it.event)
                }
                else -> view.onEventDispatched(it)
            }
        }
    }

    internal fun dispose() = subscriber?.dispose() ?: Unit

}