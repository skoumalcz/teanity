package com.skoumal.teanity.example.ui.settings

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentSettingsBinding
import com.skoumal.teanity.view.TeanityFragment
import com.skoumal.teanity.viewevents.ViewEvent
import org.koin.android.architecture.ext.viewModel

class SettingsFragment : TeanityFragment<SettingsViewModel, FragmentSettingsBinding>() {

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