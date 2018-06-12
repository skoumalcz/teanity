package com.skoumal.teanity.ui.photodetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.skoumal.teanity.ui.events.ViewEvent

class PhotoDetailViewModel : ViewModel() {

    private val _viewEvents = MutableLiveData<ViewEvent>()
    val viewEvents: LiveData<ViewEvent> get() = _viewEvents

    val text = ObservableField("")

    fun setArguments(photoId: String) {
        text.set("Detail of photo $photoId")
    }

    fun backButtonClicked() {
        _viewEvents.value = BackButtonClickedEvent()
    }
}