package com.skoumal.teanity.example.model.navigation

import com.skoumal.teanity.example.R
import com.skoumal.teanity.example.model.entity.inbound.Photo
import com.skoumal.teanity.viewevents.NavigationEvent

object Navigation {

    fun photo(photo: Photo) = NavigationEvent {
        navDirections { destination = R.id.photoDetailFragment }
        args { photoId = photo.id }
    }

}