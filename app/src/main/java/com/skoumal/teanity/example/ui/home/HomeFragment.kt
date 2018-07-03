package com.skoumal.teanity.example.ui.home

import android.os.Bundle
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

    private var recyclerScrollListener: EndlessRecyclerScrollListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerScrollListener = EndlessRecyclerScrollListener(
            binding.recycler.layoutManager,
            viewModel::loadMoreItems
        )

        binding.recycler.apply {
            addItemDecoration(
                KItemDecoration(context, LinearLayout.VERTICAL)
                    .setDeco(R.drawable.divider_1dp_gray)
            )
            addOnScrollListener(recyclerScrollListener)
        }
    }

    override fun FragmentHomeBinding.unbindViews() {
        binding.recycler.removeOnScrollListener(recyclerScrollListener)
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