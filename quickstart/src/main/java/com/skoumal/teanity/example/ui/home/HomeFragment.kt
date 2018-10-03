package com.skoumal.teanity.example.ui.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentHomeBinding
import com.skoumal.teanity.util.EndlessRecyclerScrollListener
import com.skoumal.teanity.view.TeanityFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : TeanityFragment<HomeViewModel, FragmentHomeBinding>() {

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by sharedViewModel()

    private var recyclerScrollListener: EndlessRecyclerScrollListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)

        recyclerScrollListener = EndlessRecyclerScrollListener(
            layoutManager,
            viewModel::loadMoreItems
        )

        binding.recycler.apply {
            this.layoutManager = layoutManager
            recyclerScrollListener?.let { addOnScrollListener(it) }
        }
    }

    override fun FragmentHomeBinding.unbindViews() {
        recyclerScrollListener?.let {
            recycler.removeOnScrollListener(it)
        }
    }
}