package com.skoumal.teanity.example.ui

import android.os.Bundle
import androidx.navigation.findNavController
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.ActivityMainBinding
import com.skoumal.teanity.view.TeanityActivity
import com.skoumal.teanity.viewevents.ViewEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : TeanityActivity<MainViewModel, ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    override val navController by lazy { findNavController(R.id.main_nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Config.isUserLoggedIn()) {
            navController.navigate(R.id.loginActivity)
            finish()
        }

        // TODO: Uncomment once the fix is available (https://issuetracker.google.com/issues/110692942)
        //binding.bottomNavView.setupWithNavController(navController)
    }

    override fun onEventDispatched(event: ViewEvent) {}
}
