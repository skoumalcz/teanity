package com.skoumal.teanity.example.ui.events

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.skoumal.teanity.viewevents.ViewEvent

class SnackbarEvent(
    @StringRes val text: Int,
    val length: Int = Snackbar.LENGTH_SHORT
) : ViewEvent()