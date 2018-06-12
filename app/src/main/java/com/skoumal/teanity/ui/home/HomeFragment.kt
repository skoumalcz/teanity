package com.skoumal.teanity.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.FragmentHomeBinding
import com.skoumal.teanity.model.entity.Photo
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.ui.events.ViewEventObserver
import com.skoumal.teanity.util.EndlessRecyclerScrollListener
import com.skoumal.teanity.util.KItemDecoration
import com.skoumal.teanity.util.inflateBindingView
import org.koin.android.architecture.ext.sharedViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val navController by lazy { binding.root.findNavController() }

    val viewModel: HomeViewModel by sharedViewModel()

    private val recyclerScrollListener by lazy {
        object : EndlessRecyclerScrollListener(binding.recycler.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.loadMoreItems()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = inflateBindingView<FragmentHomeBinding>(
                inflater, R.layout.fragment_home, container, false).apply {
            viewModel = this@HomeFragment.viewModel
        }

        binding.recycler.apply {
            addItemDecoration(
                    KItemDecoration(context, LinearLayout.VERTICAL)
                            .setDeco(R.drawable.divider_1dp_gray)
            )
            addOnScrollListener(recyclerScrollListener)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewEvents.observe(this, viewEventObserver)
    }

    override fun onDestroy() {
        super.onDestroy()

        // binding can be uninitialized
        if (::binding.isInitialized) {
            binding.recycler.removeOnScrollListener(recyclerScrollListener)
        }
    }

    private val viewEventObserver = ViewEventObserver {
        when (it) {
            is NavigateToPhotoDetailEvent -> {
                navController.navigate(HomeFragmentDirections.photoDetailFragment(it.photo.id))
            }
        }
    }
}

class NavigateToPhotoDetailEvent(val photo: Photo) : ViewEvent()