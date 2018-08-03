package com.skoumal.teanity.view

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.skoumal.teanity.BR
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEventObserver
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityFragment<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    Fragment(),
    TeanityView<Binding> {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected val navController by lazy { binding.root.findNavController() }
    protected val teanityActivity get() = activity as? TeanityActivity<*, *>
    private val viewEventObserver = ViewEventObserver {
        onEventDispatched(it)
        if (it is SimpleViewEvent) {
            onSimpleEventDispatched(it.event)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        restoreState(savedInstanceState)

        viewModel.viewEvents.observe(this, viewEventObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<Binding>(inflater, layoutRes, container, false).apply {
            setVariable(BR.viewModel, this@TeanityFragment.viewModel)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::binding.isInitialized) {
            binding.unbindViews()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveState(outState)
    }

    override fun saveState(outState: Bundle) {
        super.saveState(outState)
        viewModel.saveState(outState)
    }

    override fun restoreState(savedInstanceState: Bundle?) {
        super.restoreState(savedInstanceState)
        viewModel.restoreState(savedInstanceState)
    }
}
