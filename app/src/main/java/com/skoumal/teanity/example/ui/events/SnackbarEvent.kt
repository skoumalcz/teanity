package com.skoumal.teanity.example.ui.events

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import com.skoumal.teanity.viewevents.ViewEvent

class SnackbarEvent(
    @StringRes val text: Int,
    val length: Int = Snackbar.LENGTH_SHORT
) : ViewEvent()