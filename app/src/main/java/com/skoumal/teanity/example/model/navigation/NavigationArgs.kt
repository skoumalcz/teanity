package com.skoumal.teanity.example.model.navigation

import android.os.Bundle

var Bundle.photoId: String?
    get() = getString(ID.PHOTO_ID, null)
    set(value) = putString(ID.PHOTO_ID, value)

object ID {
    const val PHOTO_ID = "photo_id"
}