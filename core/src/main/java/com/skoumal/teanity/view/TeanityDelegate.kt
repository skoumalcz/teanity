package com.skoumal.teanity.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.skoumal.teanity.BR
import com.skoumal.teanity.extensions.subscribeK
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.viewevent.SimpleViewEvent
import com.skoumal.teanity.viewevent.base.ActivityExecutor
import com.skoumal.teanity.viewevent.base.ContextExecutor
import com.skoumal.teanity.viewevent.base.FragmentExecutor
import com.skoumal.teanity.viewevent.base.ViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber

internal class TeanityDelegate<V, B : ViewDataBinding, VM : TeanityViewModel>(
    private val view: V
) where V : TeanityView<B>, V : TeanityViewAccessor<VM>, V : LifecycleOwner {

    lateinit var binding: B
        private set

    private var subscriber: Disposable? = null

    private fun ensureInsets(
        target: View
    ) = ViewCompat.setOnApplyWindowInsetsListener(target) { _, insets ->
        val left = insets.systemWindowInsetLeft
        val top = insets.systemWindowInsetTop
        val right = insets.systemWindowInsetRight
        val bottom = insets.systemWindowInsetBottom

        view.peekSystemWindowInsets(Insets(left, top, right, bottom))
        val consumedInsets = view.consumeSystemWindowInsets(left, top, right, bottom)?.also {
            view.obtainViewModel().insets.value = it
        }

        consumedInsets?.let {
            insets.replaceSystemWindowInsets(
                left - it.left,
                top - it.top,
                right - it.right,
                bottom - it.bottom
            )
        } ?: insets
    }

    private fun subscribe(events: Observable<ViewEvent>) {
        subscriber = events.subscribeK(onError = { subscribe(events) }) {
            when (it) {
                is SimpleViewEvent -> {
                    Timber.e("SimpleViewEvent is deprecated. See documentation for suggested solution.")
                    view.onSimpleEventDispatched(it.event)
                }
                else -> {
                    if (it is ContextExecutor) {
                        runCatching { it(view.obtainContext()) }
                            .onFailure { t -> it.onFailure(t) }
                    }
                    if (it is FragmentExecutor) {
                        runCatching { it(view as Fragment) }
                            .onFailure { t -> it.onFailure(t) }
                    }
                    if (it is ActivityExecutor) {
                        runCatching { it(view as AppCompatActivity) }
                            .onFailure { t -> it.onFailure(t) }
                    }
                    view.onEventDispatched(it)
                }
            }
        }
    }

    internal fun dispose() = subscriber?.dispose() ?: Unit

    // ---

    @Suppress("UNUSED_PARAMETER")
    fun onCreate(dialog: Fragment, savedInstanceState: Bundle?) {
        view.restoreState(savedInstanceState)

        subscribe(view.obtainViewModel().viewEvents)
    }

    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DataBindingUtil.inflate<B>(inflater, view.obtainLayoutRes(), container, false).apply {
        setVariable(BR.viewModel, view.obtainViewModel())
        lifecycleOwner = view
    }.also { binding = it }.root.also { ensureInsets(it) }

    fun onCreate(activity: AppCompatActivity, savedInstanceState: Bundle?) {
        view.restoreState(savedInstanceState)

        binding = DataBindingUtil.setContentView<B>(activity, view.obtainLayoutRes()).apply {
            setVariable(BR.viewModel, view.obtainViewModel())
            lifecycleOwner = activity
        }

        subscribe(view.obtainViewModel().viewEvents)
        ensureInsets(binding.root)
    }

    fun onResume() {
        view.obtainViewModel().requestRefresh()
    }

    fun onDestroy() {
        if (::binding.isInitialized) {
            view.apply {
                binding.unbindViews()
            }
        }

        dispose()
    }

}