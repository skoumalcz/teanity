package com.skoumal.teanity.ui.photodetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.FragmentPhotoDetailBinding
import com.skoumal.teanity.ui.events.ViewEvent
import com.skoumal.teanity.ui.events.ViewEventObserver
import com.skoumal.teanity.util.inflateBindingView
import org.koin.android.architecture.ext.viewModel

class PhotoDetailFragment : Fragment() {

    private lateinit var binding: FragmentPhotoDetailBinding
    private val navController by lazy { binding.root.findNavController() }

    val viewModel: PhotoDetailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = inflateBindingView<FragmentPhotoDetailBinding>(
                inflater, R.layout.fragment_photo_detail, container, false).apply {
            viewModel = this@PhotoDetailFragment.viewModel
        }

        PhotoDetailFragmentArgs.fromBundle(arguments).run {
            viewModel.setArguments(photo_id)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewEvents.observe(this, viewEventObserver)
    }

    private val viewEventObserver = ViewEventObserver {
        when (it) {
            is BackButtonClickedEvent -> navController.navigateUp()
        }
    }
}

class BackButtonClickedEvent : ViewEvent()