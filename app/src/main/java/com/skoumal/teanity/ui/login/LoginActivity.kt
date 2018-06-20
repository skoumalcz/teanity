package com.skoumal.teanity.ui.login

import androidx.navigation.findNavController
import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.ActivityLoginBinding
import com.skoumal.teanity.ui.base.BaseActivity
import com.skoumal.teanity.ui.events.ViewEvent
import org.koin.android.architecture.ext.viewModel

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    override val layoutRes: Int = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModel()
    override val navController by lazy { findNavController(R.id.login_nav_host) }

    override fun onEventDispatched(event: ViewEvent) {}

}














