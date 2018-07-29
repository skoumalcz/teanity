package com.skoumal.teanity.example.ui.login

import androidx.navigation.findNavController
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.ActivityLoginBinding
import com.skoumal.teanity.view.TeanityActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : TeanityActivity<LoginViewModel, ActivityLoginBinding>() {
    override val layoutRes: Int = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModel()
    override val navController by lazy { findNavController(R.id.login_nav_host) }
}














