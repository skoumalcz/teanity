package com.skoumal.teanity.view

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.skoumal.teanity.viewevent.GenericNavDirections
import com.skoumal.teanity.viewevent.NavigationEvent
import com.skoumal.teanity.viewevent.SnackbarEvent
import com.skoumal.teanity.viewevent.base.ViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityActivity<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    AppCompatActivity(), TeanityView<Binding>, TeanityViewAccessor<ViewModel> {

    protected val binding: Binding get() = delegate.binding

    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel

    open val snackbarView get() = binding.root
    protected open val navHostId: Int = 0

    protected val navController: NavController
        get() {
            check(navHostId != 0) {
                "You must override \"navHostId\" if you want to use navController"
            }
            return findNavController(navHostId)
        }

    private val delegate by lazy { TeanityDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(this, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        delegate.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }

    //region TeanityView

    @CallSuper
    override fun onEventDispatched(event: ViewEvent) {
        when (event) {
            is NavigationEvent -> event.navigate()
            is SnackbarEvent -> event.consume(this)
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

    //endregion
    //region TeanityViewAccessor

    override fun obtainViewModel() = viewModel
    override fun obtainLayoutRes() = layoutRes
    override fun obtainContext() = this

    //endregion
    //region Helpers

    protected fun detachEvents() = delegate.dispose()
    protected fun ViewEvent.onSelf() = viewModel.run { publish() }

    //endregion
    //region Navigation

    @Deprecated("Use NavDirections.navigate() instead. Duplicating NavigationEvent is no longer necessary.")
    open fun NavigationEvent.navigate() {
        navController.navigate(navDirections, navOptions, getExtras(this@TeanityActivity))
        if (navDirections is GenericNavDirections && navDirections.clearTask) {
            finish()
        }
    }

    open fun NavDirections.navigate() {
        navController.navigate(this)
    }

    //endregion
}
