package com.skoumal.teanity.ui.login

import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.FragmentLoginEmailBinding
import com.skoumal.teanity.ui.base.BaseFragment
import com.skoumal.teanity.ui.events.SnackbarEvent
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.util.snackbar
import org.koin.android.architecture.ext.viewModel

class LoginEmailFragment : BaseFragment<LoginEmailViewModel, FragmentLoginEmailBinding>() {

    override val layoutRes: Int = R.layout.fragment_login_email
    override val viewModel: LoginEmailViewModel by viewModel()

    override fun onEventDispatched(event: ViewEvent) {
        when (event) {
            is SnackbarEvent -> snackbar(binding.root, event.text, event.length)
            is NavigateToMainActivityEvent -> {
                navController.navigate(LoginEmailFragmentDirections.mainActivity())

                // This should be part of navigation action in XML,
                // but I cannot figure out how to do it if its possible
                activity?.finish()
            }
        }
    }
}

class NavigateToMainActivityEvent : ViewEvent()