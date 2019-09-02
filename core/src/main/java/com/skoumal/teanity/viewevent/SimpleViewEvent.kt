package com.skoumal.teanity.viewevent

import com.skoumal.teanity.viewevent.base.ViewEvent

@Deprecated("Use static objects instead of integers.")
class SimpleViewEvent(
    val event: Int
) : ViewEvent()