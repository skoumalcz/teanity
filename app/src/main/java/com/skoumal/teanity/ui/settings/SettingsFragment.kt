package com.skoumal.teanity.ui.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.FragmentSettingsBinding
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.ui.events.ViewEventObserver
import com.skoumal.teanity.util.inflateBindingView
import org.koin.android.architecture.ext.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val navController by lazy { binding.root.findNavController() }

    val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = inflateBindingView<FragmentSettingsBinding>(
                inflater, R.layout.fragment_settings, container, false).apply {
            viewModel = this@SettingsFragment.viewModel
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewEvents.observe(this, viewEventObserver)
    }

    private val viewEventObserver = ViewEventObserver {
        when (it) {
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