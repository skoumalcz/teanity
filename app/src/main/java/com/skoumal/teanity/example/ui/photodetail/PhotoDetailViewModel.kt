package com.skoumal.teanity.example.ui.photodetail

import com.skoumal.teanity.util.KObservableField
import com.skoumal.teanity.viewmodel.TeanityViewModel

class PhotoDetailViewModel : TeanityViewModel() {

    val text = KObservableField("")

    fun setArguments(photoId: String) {
        text.value = "Detail of photo $photoId"
    }

    fun backButtonClicked() {
        PhotoDetailFragment.EVENT_BACK_BUTTON_CLICKED.publish()
    }
}