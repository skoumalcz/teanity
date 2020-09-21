package com.skoumal.teanity.viewmodel

import android.os.SystemClock
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.skoumal.teanity.BR
import com.skoumal.teanity.lifecycle.LiveDataObserverHost
import com.skoumal.teanity.observable.Broadcastable
import com.skoumal.teanity.observable.Notifyable
import com.skoumal.teanity.observable.observable
import com.skoumal.teanity.tools.log.info
import com.skoumal.teanity.util.Insets
import com.skoumal.teanity.view.TeanityActivity
import com.skoumal.teanity.view.TeanityFragment
import com.skoumal.teanity.viewevent.ActivityContractHelper
import com.skoumal.teanity.viewevent.NavigationEventHelper
import com.skoumal.teanity.viewevent.base.ViewEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class TeanityViewModel : ViewModel(),
    Notifyable by Notifyable.impl,
    LiveDataObserverHost by LiveDataObserverHost.impl,
    Broadcastable<ViewEvent> by Broadcastable.impl() {

    var insets by observable(Insets(), BR.insets)
        @Bindable get

    internal var lastRefresh = 0L
    private var currentJob: Job? = null

    protected open val minRefreshDelay = 0

    override fun onCleared() {
        disposeBroadcastChannel()
        super.onCleared()
    }

    /**
     * Never call this function directly unless you know what you're doing.
     * Instead you'd call [requestRefresh] which ensures refreshes never overlap.
     *
     * On top of that [refresh] is called on main thread within coroutine. Feel free to launch more coroutines,
     * this is however to trim unnecessary launch block.
     * */
    protected open suspend fun refresh() = Unit

    @CallSuper
    internal open suspend fun induceRefresh() = refresh()

    /**
     * Issues a request that will immediately invoke [induceRefresh] and consecutively [refresh] unless conditions
     * for refreshing are not met.
     *
     * 1) ### Only _one_ job can refresh data at once.
     *
     *      This rule is especially important if you're refreshing your list/recycler data which (in overlapping tasks)
     *      might encounter nasty inconsistencies. On contrary running overlapping refresh tasks is most likely
     *      an error caused by a developer.
     *
     * 2) ### Minimal refresh delay
     *
     *      Considering existence of manual refresh mechanisms this feels mandatory to include since users are users
     *      and they like to swipe to refresh repeatedly. Using override of [minRefreshDelay] you can adjust
     *      (or disable) the minimal delay.
     *
     *      By default [minRefreshDelay]=0, and can be enabled by setting it to >0. The [minRefreshDelay] is specified in millis.
     *
     * Please note that [refresh] is not guaranteed to run after issuing refresh request. This can be problematic for
     * when you've your UI logic (namely resetting) placed within [refresh] method and using a view that controls its
     * own state at the same time. This can be, however, easily overcame by checking for a result of this method which
     * implies whether or not what the refresh started. Whenever it returns true, there's a guarantee that [refresh]
     * will be called, if not you'd probably set that view to idle state manually.
     *
     * [induceRefresh] is used privately within this library to adjust behavior for different types of ViewModels.
     * For instance [LoadingViewModel] would set its state to [State.LOADING] if it wasn't ran before, ensuring that
     * user will see loader just whenever there's nothing to display. Moreover it will automatically set itself
     * to [State.LOADED] as it completes.
     * */
    @Synchronized
    fun requestRefresh(): Boolean {
        if (currentJob?.isActive == true) {
            info("Data cannot be refreshed concurrently. Request to refresh is denied.")
            return false
        }

        if (SystemClock.uptimeMillis() - lastRefresh < minRefreshDelay) {
            info("Data cannot be refreshed at this time, minimal refresh delay haven't expired yet. Request to refresh is denied.")
            return false
        }

        lastRefresh = SystemClock.uptimeMillis()
        currentJob = viewModelScope.launch {
            induceRefresh()
        }
        return true
    }

    /**
     * Wraps nav directions in [NavigationEventHelper] allowing navigation to be broadcasted from
     * viewModels. Owner activity **must be** child of [TeanityActivity] otherwise the event will
     * not be executed.
     *
     * @see TeanityActivity
     * @see NavigationEventHelper
     * */
    fun NavDirections.publish() = NavigationEventHelper(this).publish()

    /**
     * Wraps contract with [ActivityContractHelper] and suspends the execution whilst awaiting the
     * contract to be executed. Owner activity **must be** child of [TeanityActivity] (or fragment
     * [TeanityFragment]), otherwise you are required to execute the event manually.
     *
     * This allows you to request permissions >in place< bypassing the usual flows of requesting
     * and then catching the result in the activity itself.
     * */
    suspend fun <In, Out> ActivityResultContract<In, Out>.await(
        input: In
    ) = suspendCoroutine<Out> { cont ->
        ActivityContractHelper(this, input, cont::resume).publish()
    }

}