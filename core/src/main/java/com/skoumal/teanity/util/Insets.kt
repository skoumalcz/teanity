package com.skoumal.teanity.util

import androidx.core.graphics.Insets as AndroidInsets

@Suppress("FunctionName")
fun Insets(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
) = AndroidInsets.of(left, top, right, bottom)