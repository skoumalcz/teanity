package com.skoumal.teanity.viewevent.base

import androidx.appcompat.app.AppCompatActivity

/** @see ContextExecutor */
interface ActivityExecutor : OnFailureExecutor {

    /** @see [ContextExecutor.invoke] */
    operator fun invoke(activity: AppCompatActivity)

}