package com.skoumal.teanity.view

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.skoumal.teanity.BR
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.viewevent.GenericNavDirections
import com.skoumal.teanity.viewevent.NavigationEvent
import com.skoumal.teanity.viewevent.SnackbarEvent
import com.skoumal.teanity.viewevent.base.ViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityActivity<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    AppCompatActivity(),
    TeanityView<Binding> {

    protected lateinit var binding: Binding

    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel

    open val snackbarView get() = binding.root
    protected open val navHostId: Int = 0

    protected val navController: NavController
        get() {
            check(navHostId != 0) { "You must override \"navHostId\" if you want to use navController" }
            return findNavController(navHostId)
        }

    private val delegate by lazy { TeanityDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        restoreState(savedInstanceState)

        binding = DataBindingUtil.setContentView<Binding>(this, layoutRes).apply {
            setVariable(BR.viewModel, this@TeanityActivity.viewModel)
            lifecycleOwner = this@TeanityActivity
        }

        delegate.subscribe(viewModel.viewEvents)
        delegate.ensureInsets(binding.root) {
            viewModel.insets.value = it
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::binding.isInitialized) {
            binding.unbindViews()
        }

        delegate.dispose()
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
            is SnackbarEvent -> event.consume(this)
        }
    }

    override fun consumeSystemWindowInsets(left: Int, top: Int, right: Int, bottom: Int) =
        Insets.empty

    protected fun detachEvents() = delegate.dispose()
    protected fun ViewEvent.onSelf() {
        viewModel.apply { publish() }
    }

    private fun NavigationEvent.navigate() {
        navController.navigate(navDirections, navOptions, getExtras(this@TeanityActivity))
        if (navDirections is GenericNavDirections && navDirections.clearTask) {
            finish()
        }
    }
}
