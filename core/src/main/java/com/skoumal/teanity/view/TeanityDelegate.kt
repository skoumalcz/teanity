package com.skoumal.teanity.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.skoumal.teanity.BR
import com.skoumal.teanity.extensions.toInternal
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.util.InsetsResources
import com.skoumal.teanity.viewevent.base.*
import com.skoumal.teanity.viewmodel.TeanityViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

@ExperimentalCoroutinesApi
internal class TeanityDelegate<V, B : ViewDataBinding, VM : TeanityViewModel>(
    private val view: V
) where V : TeanityView<B>, V : TeanityViewAccessor<VM>, V : LifecycleOwner {

    var binding: B? = null
        private set

    private var events: ReceiveChannel<ViewEvent>? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private fun ensureInsets(
        target: View
    ) {
        val speculativeInsets = InsetsResources.insets
        if (speculativeInsets != null) {
            view.obtainViewModel().insets = speculativeInsets
        }

        ViewCompat.setOnApplyWindowInsetsListener(target) { _, insets ->
            val ourInsets = insets.toInternal()

            view.peekSystemWindowInsets(ourInsets)
            val consumedInsets = view.consumeSystemWindowInsets(ourInsets)?.also {
                view.obtainViewModel().insets = it
                InsetsResources.insets = it
            }

            consumedInsets?.let {
                Insets(
                    ourInsets.left - it.left,
                    ourInsets.top - it.top,
                    ourInsets.right - it.right,
                    ourInsets.bottom - it.bottom
                ).let { newInsets ->
                    WindowInsetsCompat.Builder()
                        .setSystemWindowInsets(newInsets)
                        .build()
                }
            } ?: insets
        }
    }

    private fun subscribe(events: ReceiveChannel<ViewEvent>) {
        this.events = events
        GlobalScope.launch {
            events.consumeAsFlow().collect {
                withContext(Dispatchers.Main) {
                    it.consumeIfInstanceCatching<FragmentExecutor> {
                        it(view as Fragment)
                    }.consumeIfInstanceCatching<ActivityExecutor> {
                        when (val v = view) {
                            is AppCompatActivity -> it(v)
                            is Fragment -> it(v.requireActivity() as AppCompatActivity)
                            else -> throw IllegalStateException()
                        }
                    }.consumeIfInstanceCatching<ContextExecutor> {
                        it(view.obtainContext())
                    }.consumeIfInstanceCatching<ViewEvent> { _ ->
                        view.onEventDispatched(it)
                    }
                }
            }
        }
    }

    internal fun detachEvents() {
        events = null
    }

    // ---

    @Suppress("UNUSED_PARAMETER")
    fun onCreate(dialog: Fragment) {
        subscribe(view.obtainViewModel().openSubscription())
    }

    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DataBindingUtil.inflate<B>(inflater, view.obtainLayoutRes(), container, false).apply {
        setVariable(BR.viewModel, view.obtainViewModel())
        lifecycleOwner = view
    }.also { binding = it }.root.also { ensureInsets(it) }

    fun onCreate(activity: AppCompatActivity) {
        binding = DataBindingUtil.setContentView<B>(activity, view.obtainLayoutRes()).apply {
            setVariable(BR.viewModel, view.obtainViewModel())
            lifecycleOwner = activity
        }

        subscribe(view.obtainViewModel().openSubscription())
        ensureInsets(binding!!.root)
    }

    fun onResume() {
        view.obtainViewModel().requestRefresh()
    }

    fun onDestroy() {
        val binding = binding
        if (binding != null) {
            view.apply {
                binding.unbindViews()
                binding.unbind()
            }
            this.binding = null
        }

        detachEvents()
    }

}