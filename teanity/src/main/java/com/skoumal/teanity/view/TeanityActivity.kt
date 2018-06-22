package com.skoumal.teanity.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.NavController
import com.evernote.android.state.StateSaver
import com.skoumal.teanity.BR
import com.skoumal.teanity.viewevents.ViewEvent
import com.skoumal.teanity.viewevents.ViewEventObserver
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityActivity<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    AppCompatActivity() {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected abstract val navController: NavController
    private val viewEventObserver = ViewEventObserver {
        onEventDispatched(it)
        if (!it.handled) {
            Log.w(
                "TeanityActivity",
                "ViewEvent ${it.javaClass.simpleName} not handled! Override onEventDispatched(ViewEvent) to handle incoming events"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        restoreState(savedInstanceState)

        binding = DataBindingUtil.setContentView<Binding>(this, layoutRes).apply {
            setVariable(BR.viewModel, this@TeanityActivity.viewModel)
        }

        viewModel.viewEvents.observe(this, viewEventObserver)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::binding.isInitialized) {
            binding.unbindViews()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (outState != null) {
            saveState(outState)
        }
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