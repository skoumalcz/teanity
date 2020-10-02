package com.skoumal.teanity.view.manager

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.skoumal.teanity.observable.Broadcastable
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.util.InsetsResources
import com.skoumal.teanity.util.toPlatform
import com.skoumal.teanity.viewevent.base.*
import com.skoumal.teanity.viewmodel.TeanityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
internal suspend fun ViewModel.subscribe(view: Any) = when (this) {
    is Broadcastable<*> -> openSubscription()
    else -> null
}?.apply {
    consumeAsFlow()
        .filterIsInstance<ViewEvent>()
        .collect { it.consume(view) }
}

// ---

internal fun View.applyInsets(
    view: Any,
    viewModel: ViewModel
) {
    if (viewModel !is TeanityViewModel) return
    if (view !is TeanityViewManager.InsetConsumer) return

    val speculativeInsets = InsetsResources.insets
    if (speculativeInsets != null) {
        viewModel.insets = speculativeInsets
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { _, _insets ->
        val insets = _insets.toPlatform()

        view.peekSystemWindowInsets(insets)
        val consumed = view.consumeSystemWindowInsets(insets)?.also {
            viewModel.insets = it
            InsetsResources.insets = it
        } ?: return@setOnApplyWindowInsetsListener _insets

        val systemInsets = Insets(
            left = insets.left - consumed.left,
            top = insets.top - consumed.top,
            right = insets.right - consumed.right,
            bottom = insets.bottom - consumed.bottom
        )

        WindowInsetsCompat.Builder()
            .setSystemWindowInsets(systemInsets)
            .build()
    }
}

// ---

private suspend fun <A> ViewEvent.consume(view: A) {
    withContext(Dispatchers.Main) {
        consumeIfInstanceCatching<FragmentExecutor> {
            it(view as Fragment)
        }.consumeIfInstanceCatching<ActivityExecutor> {
            when (view) {
                is AppCompatActivity -> it(view)
                is Fragment -> it(view.requireActivity() as AppCompatActivity)
                else -> throw IllegalStateException()
            }
        }.consumeIfInstanceCatching<ContextExecutor> {
            when (view) {
                is Activity -> it(view)
                is Fragment -> it(view.requireContext())
                else -> throw IllegalStateException()
            }
        }
    }
}