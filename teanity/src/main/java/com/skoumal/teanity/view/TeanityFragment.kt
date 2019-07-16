package com.skoumal.teanity.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.transition.TransitionInflater
import com.skoumal.teanity.BR
import com.skoumal.teanity.extensions.snackbar
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.viewevents.GenericNavDirections
import com.skoumal.teanity.viewevents.NavigationEvent
import com.skoumal.teanity.viewevents.SnackbarEvent
import com.skoumal.teanity.viewevents.ViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityFragment<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    Fragment(),
    TeanityView<Binding> {

    protected lateinit var binding: Binding

    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel

    protected open val snackbarView get() = binding.root
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

        restoreState(savedInstanceState)

        delegate.subscribe(viewModel.viewEvents)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<Binding>(inflater, layoutRes, container, false).apply {
            setVariable(BR.viewModel, this@TeanityFragment.viewModel)
            lifecycleOwner = this@TeanityFragment
        }

        delegate.ensureInsets(binding.root) {
            viewModel.insets.value = it
        }

        return binding.root
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
            is SnackbarEvent -> snackbar(snackbarView, event.message(requireContext()), event.length, event.f)
        }
    }

    protected fun detachEvents() = delegate.dispose()

    override fun consumeSystemWindowInsets(left: Int, top: Int, right: Int, bottom: Int) = Insets.empty

    private fun NavigationEvent.navigate() {
        navController.navigate(navDirections, navOptions, getExtras(this@TeanityFragment))
        if (navDirections is GenericNavDirections && navDirections.clearTask) {
            activity?.finish()
        }
    }
}
