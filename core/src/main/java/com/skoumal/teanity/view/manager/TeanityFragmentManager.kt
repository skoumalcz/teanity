package com.skoumal.teanity.view.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.skoumal.teanity.BR
import com.skoumal.teanity.lifecycle.launchWhenDestroyed
import com.skoumal.teanity.viewmodel.TeanityViewModel
import kotlinx.coroutines.channels.ReceiveChannel

internal class TeanityFragmentManager<F, B> : TeanityViewManager<F, B>
        where F : Fragment, F : TeanityViewManager.Props,
              B : ViewDataBinding {

    private var subscription: ReceiveChannel<*>? = null
        set(value) {
            field?.cancel()
            field = value
        }
    override lateinit var binding: B

    override fun attach(view: F) {
        if (view is TeanityViewManager.ViewCreator) {
            view.addViewCreator(object : TeanityViewManager.ViewCreator.Callback {
                override fun createView(
                    inflater: LayoutInflater,
                    parent: ViewGroup?
                ): View = DataBindingUtil.inflate<B>(
                    inflater,
                    view.layoutRes,
                    parent,
                    false
                ).apply {
                    setVariable(BR.viewModel, view.viewModel)
                    lifecycleOwner = view
                    root.applyInsets(view, view.viewModel)
                    binding = this
                }.root
            })
        }

        with(view.lifecycleScope) {
            launchWhenStarted {
                subscription = view.viewModel.subscribe(view)
            }
            launchWhenDestroyed(view.lifecycle) {
                detach()
            }
        }
    }

    override fun resume(view: F) {
        when (val vm = view.viewModel) {
            is TeanityViewModel -> vm.requestRefresh()
        }
    }

    override fun detach() {
        if (::binding.isInitialized) {
            binding.unbind()
        }
        subscription = null
    }
}