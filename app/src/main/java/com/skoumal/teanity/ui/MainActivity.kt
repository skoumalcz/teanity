package com.skoumal.teanity.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.skoumal.teanity.Config
import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.ActivityMainBinding
import com.skoumal.teanity.ui.base.BaseActivity
import com.skoumal.teanity.ui.events.ViewEvent
import org.koin.android.architecture.ext.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    override val navController by lazy { findNavController(R.id.main_nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Config.isUserLoggedIn()) {
            navController.navigate(R.id.loginActivity)
            finish()
        }

        binding.bottomNavView.setupWithNavController(navController)
    }

    override fun onEventDispatched(event: ViewEvent) {}
}
