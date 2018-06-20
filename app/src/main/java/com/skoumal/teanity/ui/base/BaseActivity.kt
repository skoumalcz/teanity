package com.skoumal.teanity.ui.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.ui.events.ViewEventObserver
import com.skoumal.teanity.util.setBindingContentView
import me.tatarka.bindingcollectionadapter2.BR

abstract class BaseActivity<ViewModel : BaseViewModel, Binding : ViewDataBinding> :
    AppCompatActivity() {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected abstract val navController: NavController
    private val viewEventObserver = ViewEventObserver {
        onEventDispatched(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = setBindingContentView<Binding>(layoutRes).apply {
            setVariable(BR.viewModel, this@BaseActivity.viewModel)
        }

        viewModel.viewEvents.observe(this, viewEventObserver)
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