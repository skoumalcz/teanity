package com.skoumal.teanity.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import com.skoumal.teanity.BR
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewevents.ViewEventObserver
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityActivity<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    AppCompatActivity(),
    TeanityView<Binding> {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected abstract val navController: NavController
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

    override fun saveState(outState: Bundle) {
        super.saveState(outState)
        viewModel.saveState(outState)
    }

    override fun restoreState(savedInstanceState: Bundle?) {
        super.restoreState(savedInstanceState)
        viewModel.restoreState(savedInstanceState)
    }
}
