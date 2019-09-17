package com.skoumal.teanity.viewevent

import com.skoumal.teanity.viewevent.base.ViewEvent

@Deprecated("Use static objects with sealed classes instead of integers.")
class SimpleViewEvent(
    val event: Int
) : ViewEvent()