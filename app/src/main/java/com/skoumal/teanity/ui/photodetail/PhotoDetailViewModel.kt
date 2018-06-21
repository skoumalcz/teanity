package com.skoumal.teanity.ui.photodetail

import android.databinding.ObservableField
import com.skoumal.teanity.ui.base.BaseViewModel

class PhotoDetailViewModel : BaseViewModel() {

    val text = ObservableField("")

    fun setArguments(photoId: String) {
        text.set("Detail of photo $photoId")
    }

    fun backButtonClicked() {
        BackButtonClickedEvent().publish()
    }
}