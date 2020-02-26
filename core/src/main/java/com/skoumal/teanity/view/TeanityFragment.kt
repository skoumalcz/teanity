package com.skoumal.teanity.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.transition.TransitionInflater
import com.skoumal.teanity.viewevent.GenericNavDirections
import com.skoumal.teanity.viewevent.NavigationEvent
import com.skoumal.teanity.viewevent.SnackbarEvent
import com.skoumal.teanity.viewevent.base.ViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityFragment<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    Fragment(), TeanityView<Binding>, TeanityViewAccessor<ViewModel> {

    protected val binding: Binding get() = delegate.binding

    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel

    open val snackbarView get() = binding.root
    protected open val isTransitionsEnabled = false

    protected val navController get() = binding.root.findNavController()
    protected val teanityActivity get() = activity as? TeanityActivity<*, *>

    private val delegate by lazy { TeanityDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isTransitionsEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sharedElementEnterTransition = TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.move)
            }
        }

        delegate.onCreate(this, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = delegate.onCreateView(inflater, container)

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
    override fun obtainContext() = requireContext()

    //endregion
    //region Helpers

    protected fun detachEvents() = delegate.detachEvents()
    protected fun ViewEvent.onSelf() {
        viewModel.apply { publish() }
    }

    private fun NavigationEvent.navigate() {
        navController.navigate(navDirections, navOptions, getExtras(this@TeanityFragment))
        if (navDirections is GenericNavDirections && navDirections.clearTask) {
            activity?.finish()
        }
    }

    //endregion
}
