package com.skoumal.teanity.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.skoumal.teanity.view.manager.TeanityViewManager

abstract class TeanityDialogFragment<Binding : ViewDataBinding> :
    DialogFragment(),
    TeanityViewManager.ViewCreator
    by TeanityViewManager.ViewCreator.delegate,
    TeanityViewManager.Props,
    TeanityViewManager.InsetConsumer,
    TeanityViewManager<TeanityDialogFragment<Binding>, Binding>
    by TeanityViewManager.fragment<TeanityDialogFragment<Binding>, Binding>() {

    protected val navController: NavController
        get() = NavHostRetriever.findNavController(this)

    protected val teanityActivity: TeanityActivity<*>
        get() = requireActivity() as TeanityActivity<*>

    protected open val dialogStyle = STYLE_NORMAL
    protected open val dialogTheme = 0

    init {
        @Suppress("LeakingThis")
        attach(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(dialogStyle, dialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = createView(inflater, container!!)

    override fun onResume() {
        super.onResume()
        resume(this)
    }

    open fun NavDirections.navigate() {
        navController.navigate(this)
    }

}
