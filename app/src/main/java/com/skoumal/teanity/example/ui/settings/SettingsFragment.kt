package com.skoumal.teanity.example.ui.settings

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentSettingsBinding
import com.skoumal.teanity.view.TeanityFragment
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : TeanityFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val layoutRes: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by viewModel()

    override fun onSimpleEventDispatched(event: Int) {
        when (event) {
            EVENT_NAVIGATE_TO_LOGIN_ACTIVITY -> {
                navController.navigate(SettingsFragmentDirections.loginActivity())

                // This should be part of navigation action in XML,
                // but I cannot figure out how to do it if its possible
                activity?.finish()
            }
        }
    }

    companion object {
        const val EVENT_NAVIGATE_TO_LOGIN_ACTIVITY = 1
    }
}
