package com.skoumal.teanity.example.model.base

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.model.entity.inbound.Photo
import com.skoumal.teanity.example.model.navigation.photoId
import com.skoumal.teanity.viewevents.NavigationEvent
import com.skoumal.teanity.viewmodel.LoadingViewModel

abstract class ExampleViewModel : LoadingViewModel() {

    fun photoDetail(photo: Photo) =
        NavigationEvent {
            navDirections { destination = R.id.photoDetailFragment }
            args { photoId = photo.id }
        }.publish()

}