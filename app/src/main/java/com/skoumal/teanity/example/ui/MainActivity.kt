package com.skoumal.teanity.example.ui

import android.os.Bundle
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.ActivityMainBinding
import com.skoumal.teanity.example.util.setupWith
import com.skoumal.teanity.view.TeanityActivity
import com.skoumal.teanity.viewevents.ViewEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : TeanityActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    override val navHostId = R.id.main_nav_host

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Config.isUserLoggedIn()) {
            navController.navigate(R.id.loginActivity)
            finish()
        }

        binding.bottomNavView.setupWith(navController)
    }

    override fun onEventDispatched(event: ViewEvent) {}
}
