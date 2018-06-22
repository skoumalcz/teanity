package com.skoumal.teanity.example.ui.photodetail

import android.databinding.ObservableField
import com.skoumal.teanity.viewmodel.TeanityViewModel

class PhotoDetailViewModel : TeanityViewModel() {

    val text = ObservableField("")

    fun setArguments(photoId: String) {
        text.set("Detail of photo $photoId")
    }

    fun backButtonClicked() {
        BackButtonClickedEvent().publish()
    }
}