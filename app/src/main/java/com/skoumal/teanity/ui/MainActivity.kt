package com.skoumal.teanity.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.skoumal.teanity.Config
import com.skoumal.teanity.R
import org.koin.android.architecture.ext.viewModel

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.main_nav_host) }

    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (!Config.isUserLoggedIn()) {
            navController.navigate(R.id.loginActivity)
            finish()
        }

        findViewById<BottomNavigationView>(R.id.bottom_nav_view).setupWithNavController(navController)
    }
}
