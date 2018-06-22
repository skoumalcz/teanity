package com.skoumal.teanity.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.BR
import com.skoumal.teanity.viewevents.ViewEvent
import com.skoumal.teanity.viewevents.ViewEventObserver
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityFragment<ViewModel : TeanityViewModel, Binding : ViewDataBinding> : Fragment() {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected val navController by lazy { binding.root.findNavController() }
    private val viewEventObserver = ViewEventObserver {
        onEventDispatched(it)
        if (!it.handled) {
            Log.w(
                "TeanityFragment",
                "ViewEvent ${it.javaClass.simpleName} not handled! Override onEventDispatched(ViewEvent) to handle incoming events"
            )
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

    open fun onEventDispatched(event: ViewEvent) {}

    open fun Binding.unbindViews() {}

    /**
     * Override this method if you have more viewModels or anything else you want to restore
     * You should also override [saveState]
     */
    @CallSuper
    open fun restoreState(savedInstanceState: Bundle?) {
        StateSaver.restoreInstanceState(this, savedInstanceState)
        viewModel.restoreState(savedInstanceState)
    }

    /**
     * Override this method if you have more viewModels or anything else you want to save
     * You should also override [restoreState]
     */
    @CallSuper
    open fun saveState(outState: Bundle) {
        StateSaver.saveInstanceState(this, outState);
        viewModel.saveState(outState)
    }

}