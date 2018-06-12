package com.skoumal.teanity.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.FragmentLoginEmailBinding
import com.skoumal.teanity.ui.events.SnackbarEvent
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.ui.events.ViewEventObserver
import com.skoumal.teanity.util.inflateBindingView
import com.skoumal.teanity.util.snackbar
import org.koin.android.architecture.ext.viewModel

class LoginEmailFragment : Fragment() {

    private lateinit var binding: FragmentLoginEmailBinding
    private val navController by lazy { binding.root.findNavController() }

    val viewModel: LoginEmailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = inflateBindingView<FragmentLoginEmailBinding>(
                inflater, R.layout.fragment_login_email, container, false).apply {
            viewModel = this@LoginEmailFragment.viewModel
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewEvents.observe(this, viewEventObserver)
    }

    private val viewEventObserver = ViewEventObserver {
        when (it) {
            is SnackbarEvent -> snackbar(binding.root, it.text, it.length)
            is NavigateToMainActivityEvent -> {
                navController.navigate(LoginEmailFragmentDirections.mainActivity())

                // This should be part of navigation action in XML,
                // but I cannot figure out how to do it if its possible
                activity?.finish()
            }
        }
    }
}

class NavigateToMainActivityEvent() : ViewEvent()