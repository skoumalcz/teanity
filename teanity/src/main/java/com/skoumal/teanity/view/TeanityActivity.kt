package com.skoumal.teanity.view

import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.skoumal.teanity.BR
import com.skoumal.teanity.extensions.snackbar
import com.skoumal.teanity.viewevents.*
import com.skoumal.teanity.viewmodel.TeanityViewModel
import io.reactivex.disposables.Disposable

abstract class TeanityActivity<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    AppCompatActivity(),
    TeanityView<Binding> {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected open val snackbarView get() = binding.root
    protected open val navHostId: Int = 0
    protected val navController: NavController
        get() {
            if (navHostId == 0) {
                throw IllegalStateException("You must override \"navHostId\" if you want to use navController")
            }
            return findNavController(navHostId)
        }
    private lateinit var subscriber: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        restoreState(savedInstanceState)

        binding = DataBindingUtil.setContentView<Binding>(this, layoutRes).apply {
            setVariable(BR.viewModel, this@TeanityActivity.viewModel)
            lifecycleOwner = this@TeanityActivity
        }

        subscriber = viewModel.viewEvents.subscribe({
            when (it) {
                is SimpleViewEvent -> onSimpleEventDispatched(it.event)
                else -> onEventDispatched(it)
            }
        }, {
            Log.e(this::class.java.simpleName, "No further events will be received", it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::binding.isInitialized) {
            binding.unbindViews()
        }
        if (::subscriber.isInitialized) {
            subscriber.dispose()
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
            is NavigationEvent -> event.navigate()
            is SnackbarEvent -> snackbar(snackbarView, event.message(this), event.length, event.f)
        }
    }

    protected fun detachEvents() = subscriber.dispose()

    private fun NavigationEvent.navigate() {
        navController.navigate(navDirections, navOptions)
        if (navDirections is GenericNavDirections && navDirections.clearTask) {
            finish()
        }
    }
}
