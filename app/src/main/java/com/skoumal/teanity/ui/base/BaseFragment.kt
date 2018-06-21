package com.skoumal.teanity.ui.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.skoumal.teanity.BR
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.ui.events.ViewEventObserver
import com.skoumal.teanity.util.inflateBindingView

abstract class BaseFragment<ViewModel : BaseViewModel, Binding : ViewDataBinding> : Fragment() {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected val navController by lazy { binding.root.findNavController() }
    private val viewEventObserver = ViewEventObserver {
        onEventDispatched(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewEvents.observe(this, viewEventObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = inflateBindingView<Binding>(inflater, layoutRes, container, false).apply {
            setVariable(BR.viewModel, this@BaseFragment.viewModel)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::binding.isInitialized) {
            binding.unbindViews()
        }
    }

    abstract fun onEventDispatched(event: ViewEvent)

    open fun Binding.unbindViews() {}
}