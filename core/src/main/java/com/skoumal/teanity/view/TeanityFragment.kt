package com.skoumal.teanity.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.transition.TransitionInflater
import com.skoumal.teanity.view.manager.TeanityViewManager

abstract class TeanityFragment<Binding : ViewDataBinding> :
    Fragment(),
    TeanityViewManager.ViewCreator
    by TeanityViewManager.ViewCreator.delegate,
    TeanityViewManager.Props,
    TeanityViewManager.InsetConsumer,
    TeanityViewManager<TeanityFragment<Binding>, Binding>
    by TeanityViewManager.fragment<TeanityFragment<Binding>, Binding>() {

    protected val navController: NavController
        get() = NavHostRetriever.findNavController(this)

    protected val teanityActivity: TeanityActivity<*>
        get() = requireActivity() as TeanityActivity<*>

    protected open val isTransitionsEnabled = false

    init {
        @Suppress("LeakingThis")
        attach(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isTransitionsEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sharedElementEnterTransition = TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.move)
            }
        }
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
