package com.skoumal.teanity.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.skoumal.teanity.viewevent.base.ViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel

abstract class TeanityDialogFragment<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    DialogFragment(), TeanityView<Binding>, TeanityViewAccessor<ViewModel> {

    protected val binding: Binding get() = delegate.binding

    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel

    protected val navController get() = binding.root.findNavController()
    protected val teanityActivity get() = activity as? TeanityActivity<*, *>

    private val delegate by lazy { TeanityDelegate(this) }

    protected open val dialogStyle = STYLE_NORMAL
    protected open val dialogTheme = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(dialogStyle, dialogTheme)

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

    override fun onEventDispatched(event: ViewEvent) {}

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

    //endregion
}
