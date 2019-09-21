package com.skoumal.teanity.example.ui.login

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentLoginEmailBinding
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.view.TeanityFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginEmailFragment : TeanityFragment<LoginEmailViewModel, FragmentLoginEmailBinding>() {

    override val layoutRes: Int = R.layout.fragment_login_email
    override val viewModel: LoginEmailViewModel by viewModel()

    override fun onSimpleEventDispatched(event: Int) {
        when (event) {
            EVENT_NAVIGATE_TO_MAIN_ACTIVITY -> {
                navController.navigate(LoginEmailFragmentDirections.mainActivity())

                // This should be part of navigation action in XML,
                // but I cannot figure out how to do it if its possible
                activity?.finish()
            }
        }
    }

    override fun consumeSystemWindowInsets(left: Int, top: Int, right: Int, bottom: Int) = Insets(top = top)

    companion object {
        const val EVENT_NAVIGATE_TO_MAIN_ACTIVITY = 1
    }
}
