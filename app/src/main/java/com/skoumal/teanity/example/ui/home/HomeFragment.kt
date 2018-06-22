package com.skoumal.teanity.example.ui.home

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentHomeBinding
import com.skoumal.teanity.example.model.entity.Photo
import com.skoumal.teanity.util.EndlessRecyclerScrollListener
import com.skoumal.teanity.util.KItemDecoration
import com.skoumal.teanity.view.TeanityFragment
import com.skoumal.teanity.viewevents.ViewEvent
import org.koin.android.architecture.ext.sharedViewModel

class HomeFragment : TeanityFragment<HomeViewModel, FragmentHomeBinding>() {

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by sharedViewModel()

    private val recyclerScrollListener by lazy {
        object : EndlessRecyclerScrollListener(binding.recycler.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.loadMoreItems()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.apply {
            addItemDecoration(
                KItemDecoration(context, LinearLayout.VERTICAL)
                    .setDeco(R.drawable.divider_1dp_gray)
            )
            addOnScrollListener(recyclerScrollListener)
        }
    }

    override fun FragmentHomeBinding.unbindViews() {
        recycler.removeOnScrollListener(recyclerScrollListener)
    }

    override fun onEventDispatched(event: ViewEvent) {
        when (event) {
            is NavigateToPhotoDetailEvent -> {
                navController.navigate(HomeFragmentDirections.photoDetailFragment(event.photo.id))
            }
        }
    }
}

class NavigateToPhotoDetailEvent(val photo: Photo) : ViewEvent()