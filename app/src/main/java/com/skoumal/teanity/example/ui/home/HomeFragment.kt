package com.skoumal.teanity.example.ui.home

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentHomeBinding
import com.skoumal.teanity.view.TeanityFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : TeanityFragment<HomeViewModel, FragmentHomeBinding>() {

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by sharedViewModel()

}