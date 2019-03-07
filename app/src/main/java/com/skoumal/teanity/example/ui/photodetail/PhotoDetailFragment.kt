package com.skoumal.teanity.example.ui.photodetail

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.databinding.FragmentPhotoDetailBinding
import com.skoumal.teanity.view.TeanityFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PhotoDetailFragment : TeanityFragment<PhotoDetailViewModel, FragmentPhotoDetailBinding>() {

    override val layoutRes: Int = R.layout.fragment_photo_detail
    override val viewModel: PhotoDetailViewModel by viewModel { parametersOf(args.photoId) }
    
    private val args by lazy { PhotoDetailFragmentArgs.fromBundle(requireArguments()) }

    override fun onSimpleEventDispatched(event: Int) {
        when (event) {
            EVENT_BACK_BUTTON_CLICKED -> navController.navigateUp()
        }
    }

    companion object {
        const val EVENT_BACK_BUTTON_CLICKED = 1
    }
}
