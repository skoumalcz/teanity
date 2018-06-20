package com.skoumal.teanity.ui.photodetail

import android.os.Bundle
import android.view.View
import com.skoumal.teanity.R
import com.skoumal.teanity.databinding.FragmentPhotoDetailBinding
import com.skoumal.teanity.ui.base.BaseFragment
import com.skoumal.teanity.ui.events.ViewEvent
import org.koin.android.architecture.ext.viewModel

class PhotoDetailFragment : BaseFragment<PhotoDetailViewModel, FragmentPhotoDetailBinding>() {

    override val layoutRes: Int = R.layout.fragment_photo_detail
    override val viewModel: PhotoDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PhotoDetailFragmentArgs.fromBundle(arguments).run {
            viewModel.setArguments(photo_id)
        }
    }

    override fun onEventDispatched(event: ViewEvent) {
        when (event) {
            is BackButtonClickedEvent -> navController.navigateUp()
        }
    }
}

class BackButtonClickedEvent : ViewEvent()