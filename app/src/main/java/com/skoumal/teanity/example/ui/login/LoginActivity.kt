package com.skoumal.teanity.example.ui.login

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.ActivityLoginBinding
import com.skoumal.teanity.view.TeanityActivity
import org.koin.androidx.viewmodel.ext.viewModel

class LoginActivity : TeanityActivity<LoginViewModel, ActivityLoginBinding>() {
    override val layoutRes: Int = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModel()
    override val navHostId = R.id.login_nav_host
}














