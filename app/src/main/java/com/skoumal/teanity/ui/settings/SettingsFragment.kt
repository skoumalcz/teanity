package com.skoumal.teanity.ui.settings

import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.FragmentSettingsBinding
import com.skoumal.teanity.ui.base.BaseFragment
import com.skoumal.teanity.ui.events.ViewEvent
import org.koin.android.architecture.ext.viewModel

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val layoutRes: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by viewModel()

    override fun onEventDispatched(event: ViewEvent) {
        when (event) {
            is NavigateToLoginActivityEvent -> {
                navController.navigate(SettingsFragmentDirections.loginActivity())

                // This should be part of navigation action in XML,
                // but I cannot figure out how to do it if its possible
                activity?.finish()
            }
        }
    }
}

class NavigateToLoginActivityEvent : ViewEvent()