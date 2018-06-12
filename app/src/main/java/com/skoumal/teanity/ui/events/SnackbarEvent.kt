package com.skoumal.teanity.ui.events

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar

class SnackbarEvent(
        @StringRes val text: Int,
        val length: Int = Snackbar.LENGTH_SHORT
) : ViewEvent()