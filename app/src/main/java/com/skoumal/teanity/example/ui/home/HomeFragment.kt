package com.skoumal.teanity.example.ui.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentHomeBinding
import com.skoumal.teanity.example.model.entity.Photo
import com.skoumal.teanity.util.EndlessRecyclerScrollListener
import com.skoumal.teanity.util.KItemDecoration
import com.skoumal.teanity.view.TeanityFragment
import com.skoumal.teanity.viewevents.ViewEvent
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
            addItemDecoration(
                KItemDecoration(context, LinearLayout.VERTICAL)
                    .setDeco(R.drawable.divider_1dp_gray)
            )
            recyclerScrollListener?.let { addOnScrollListener(it) }
        }
    }

    override fun FragmentHomeBinding.unbindViews() {
        recyclerScrollListener?.let {
            recycler.removeOnScrollListener(it)
        }
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