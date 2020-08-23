package com.skoumal.teanity.view.manager

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.skoumal.teanity.BR
import com.skoumal.teanity.lifecycle.launchWhenDestroyed
import com.skoumal.teanity.viewmodel.TeanityViewModel
import kotlinx.coroutines.channels.ReceiveChannel

internal class TeanityActivityManager<A, B> : TeanityViewManager<A, B>
        where A : AppCompatActivity, A : TeanityViewManager.Props,
              B : ViewDataBinding {

    private var subscription: ReceiveChannel<*>? = null
        set(value) {
            field?.cancel()
            field = value
        }
    override lateinit var binding: B

    override fun attach(view: A) {
        binding = DataBindingUtil.setContentView<B>(view, view.layoutRes).apply {
            setVariable(BR.viewModel, view.viewModel)
            lifecycleOwner = view
            root.applyInsets(view, view.viewModel)
        }
        with(view.lifecycleScope) {
            launchWhenResumed {
                when (val vm = view.viewModel) {
                    is TeanityViewModel -> vm.requestRefresh()
                }
            }
            launchWhenStarted {
                subscription = view.viewModel.subscribe(view)
            }
            launchWhenDestroyed(view.lifecycle) {
                detach()
            }
        }
    }

    override fun detach() {
        if (::binding.isInitialized) {
            binding.unbind()
        }
        subscription = null
    }
}