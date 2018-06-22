package com.skoumal.teanity.example.ui.login

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentLoginEmailBinding
import com.skoumal.teanity.example.ui.events.SnackbarEvent
import com.skoumal.teanity.extensions.snackbar
import com.skoumal.teanity.view.TeanityFragment
import com.skoumal.teanity.viewevents.ViewEvent
import org.koin.android.architecture.ext.viewModel

class LoginEmailFragment : TeanityFragment<LoginEmailViewModel, FragmentLoginEmailBinding>() {

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