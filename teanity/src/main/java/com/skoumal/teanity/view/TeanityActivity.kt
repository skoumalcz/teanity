package com.skoumal.teanity.view

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.skoumal.teanity.BR
import com.skoumal.teanity.view.navigation.navigate
import com.skoumal.teanity.viewevents.NavigationEvent
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEvent
import com.skoumal.teanity.viewevents.ViewEventObserver
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityActivity<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    AppCompatActivity(),
    TeanityView<Binding> {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected open val navHostId: Int = 0
    protected val navController: NavController
        get() {
            if (navHostId == 0) {
                throw IllegalStateException("You must override \"navHostId\" if you want to use navController")
            }
            return findNavController(navHostId)
        }
    private val viewEventObserver = ViewEventObserver {
        onEventDispatched(it)
        if (it is SimpleViewEvent) {
            onSimpleEventDispatched(it.event)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        restoreState(savedInstanceState)

        binding = DataBindingUtil.setContentView<Binding>(this, layoutRes).apply {
            setVariable(BR.viewModel, this@TeanityActivity.viewModel)
            lifecycleOwner = this@TeanityActivity
        }

        viewModel.viewEvents.observe(this, viewEventObserver)
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

    @CallSuper
    override fun onEventDispatched(event: ViewEvent) {
        when (event) {
            is NavigationEvent -> (event.destination to event.args).navigate(event.navOptions)
        }
    }

    override fun Pair<Int, Bundle>.navigate(options: NavOptions?) {
        navController.navigate(this, options)
    }
}
