package com.skoumal.teanity.viewevent

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import com.skoumal.teanity.view.TeanityActivity
import com.skoumal.teanity.viewevent.base.ActivityExecutor
import com.skoumal.teanity.viewevent.base.ViewEvent

class NavigationEventHelper(
    private val directions: NavDirections
) : ViewEvent(), ActivityExecutor {

    override fun invoke(activity: AppCompatActivity) {
        if (activity !is TeanityActivity<*, *>) return
        activity.apply { directions.navigate() }
    }

}