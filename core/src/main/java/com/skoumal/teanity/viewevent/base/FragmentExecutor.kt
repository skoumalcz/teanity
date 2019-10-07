package com.skoumal.teanity.viewevent.base

import androidx.fragment.app.Fragment

/** @see ContextExecutor */
interface FragmentExecutor : OnFailureExecutor {

    /** @see [ContextExecutor.invoke] */
    operator fun invoke(fragment: Fragment)

}