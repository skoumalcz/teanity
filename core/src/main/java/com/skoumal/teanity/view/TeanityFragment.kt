package com.skoumal.teanity.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.transition.TransitionInflater
import com.skoumal.teanity.viewevent.SuspendingActivityResultContract
import com.skoumal.teanity.viewevent.base.ViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityFragment<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    Fragment(), TeanityView<Binding>, TeanityViewAccessor<ViewModel> {

    protected val binding: Binding get() = requireNotNull(delegate.binding)

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

        delegate.onCreate(this)
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

    //region TeanityView

    override fun onEventDispatched(event: ViewEvent) {}

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

    /**
     * Registers a suspending result contract. Provided
     * [SuspendingActivityResultContract.SuspendingResult] can be called after lifecycle's been
     * CREATED.
     *
     * @see SuspendingActivityResultContract.registerIn
     * */
    protected fun <In, Out> ActivityResultContract<In, Out>.asSuspending() =
        SuspendingActivityResultContract(this).registerIn(this@TeanityFragment)

    //endregion
}
