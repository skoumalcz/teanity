package com.skoumal.teanity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.skoumal.teanity.view.manager.TeanityViewManager

abstract class TeanityActivity<Binding : ViewDataBinding> :
    AppCompatActivity(),
    TeanityViewManager.Props,
    TeanityViewManager.InsetConsumer,
    TeanityViewManager<TeanityActivity<Binding>, Binding>
    by TeanityViewManager.activity<TeanityActivity<Binding>, Binding>() {

    protected val navController: NavController
        get() = NavHostRetriever.findNavController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attach(this)
    }

    open fun NavDirections.navigate() {
        navController.navigate(this)
    }

}
