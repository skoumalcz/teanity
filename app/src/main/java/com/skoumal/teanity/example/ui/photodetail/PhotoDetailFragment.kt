package com.skoumal.teanity.example.ui.photodetail

import android.os.Bundle
import android.view.View
import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentPhotoDetailBinding
import com.skoumal.teanity.view.TeanityFragment
import org.koin.android.architecture.ext.viewModel

class PhotoDetailFragment : TeanityFragment<PhotoDetailViewModel, FragmentPhotoDetailBinding>() {

    override val layoutRes: Int = R.layout.fragment_photo_detail
    override val viewModel: PhotoDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PhotoDetailFragmentArgs.fromBundle(arguments).run {
            viewModel.setArguments(photoId)
        }
    }

    override fun onSimpleEventDispatched(event: Int) {
        when (event) {
            EVENT_BACK_BUTTON_CLICKED -> navController.navigateUp()
        }
    }

    companion object {
        const val EVENT_BACK_BUTTON_CLICKED = 1
    }
}
