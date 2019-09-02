package com.skoumal.teanity.view

import android.view.View
import androidx.core.view.ViewCompat
import com.skoumal.teanity.extensions.subscribeK
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber

internal class TeanityDelegate(private val view: TeanityView<*>) {

    private var subscriber: Disposable? = null

    internal inline fun ensureInsets(view: View, crossinline body: (Insets) -> Unit) =
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val left = insets.systemWindowInsetLeft
            val top = insets.systemWindowInsetTop
            val right = insets.systemWindowInsetRight
            val bottom = insets.systemWindowInsetBottom

            this.view.peekSystemWindowInsets(Insets(left, top, right, bottom))
            val consumedInsets = this.view.consumeSystemWindowInsets(left, top, right, bottom).also(body)

            insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft - consumedInsets.left,
                insets.systemWindowInsetTop - consumedInsets.top,
                insets.systemWindowInsetRight - consumedInsets.right,
                insets.systemWindowInsetBottom - consumedInsets.bottom
            )
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