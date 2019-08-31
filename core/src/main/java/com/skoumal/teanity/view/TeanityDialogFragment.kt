package com.skoumal.teanity.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.skoumal.teanity.BR
import com.skoumal.teanity.viewevents.SimpleViewEvent
import com.skoumal.teanity.viewmodel.TeanityViewModel
import io.reactivex.disposables.Disposable

abstract class TeanityDialogFragment<ViewModel : TeanityViewModel, Binding : ViewDataBinding> :
    DialogFragment(),
    TeanityView<Binding> {

    protected lateinit var binding: Binding
    protected abstract val layoutRes: Int
    protected abstract val viewModel: ViewModel
    protected val navController get() = binding.root.findNavController()
    protected val teanityActivity get() = activity as? TeanityActivity<*, *>
    private lateinit var subscriber: Disposable

    protected open val dialogStyle = STYLE_NORMAL
    protected open val dialogTheme = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(dialogStyle, dialogTheme)

        restoreState(savedInstanceState)

        subscriber = viewModel.viewEvents.subscribe({
            when (it) {
                is SimpleViewEvent -> onSimpleEventDispatched(it.event)
                else -> onEventDispatched(it)
            }
        }, {
            Log.e(this::class.java.simpleName, "No further events will be received", it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<Binding>(inflater, layoutRes, container, false).apply {
            setVariable(BR.viewModel, this@TeanityDialogFragment.viewModel)
            lifecycleOwner = this@TeanityDialogFragment
        }

        return binding.root
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
}