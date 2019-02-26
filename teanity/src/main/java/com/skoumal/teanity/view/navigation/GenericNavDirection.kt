package com.skoumal.teanity.view.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

class GenericNavDirections(private val target: Int, private val args: Bundle) : NavDirections {

    override fun getArguments(): Bundle = args
    override fun getActionId(): Int = target

}

fun Pair<Int, Bundle>.toNavDirections() = GenericNavDirections(first, second)

fun NavController.navigate(directions: Pair<Int, Bundle>, navOptions: NavOptions? = null) {
    navigate(directions.toNavDirections(), navOptions)
}