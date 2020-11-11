package com.skoumal.teanity.viewevent

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.skoumal.teanity.viewevent.base.ActivityExecutor
import com.skoumal.teanity.viewevent.base.ViewEvent
import kotlinx.coroutines.channels.Channel

typealias ActivityContractHelperListener<T> = (T) -> Unit

@Deprecated(
    "Helper cannot be used as contracts need to be registered before lifecycle starts.",
    level = DeprecationLevel.WARNING
)
class ActivityContractHelper<In, Out>(
    private val contract: ActivityResultContract<In, Out>,
    private val input: In,
    private val listener: ActivityContractHelperListener<Out>
) : ViewEvent(), ActivityExecutor {

    override fun invoke(activity: AppCompatActivity) {
        activity.registerForActivityResult(contract) { listener(it) }.launch(input)
    }

}

class SuspendingActivityResultContract<In, Out>(
    private val contract: ActivityResultContract<In, Out>
) {

    /**
     * Registers this [contract] for result. According to androidx.activity docs, this is only safe
     * to call before lifecycle state is @ STARTED. Provided [SuspendingResult] can be called at any
     * time thereafter.
     *
     * Make sure to call this in or before your [ComponentActivity.onStart] or as a class property
     * initialized at construction time.
     * */
    fun registerIn(activity: ComponentActivity): SuspendingResult {
        val channel = Channel<Out>()
        val callback = ActivityResultCallback<Out> { channel.offer(it) }
        val launcher = activity.registerForActivityResult(contract, callback)
        return SuspendingResult(launcher, channel)
    }

    /** @see registerIn */
    fun registerIn(fragment: Fragment) =
        registerIn(fragment.requireActivity())

    inner class SuspendingResult(
        private val launcher: ActivityResultLauncher<In>,
        private val channel: Channel<Out>
    ) {

        suspend operator fun invoke(input: In): Out {
            launcher.launch(input)
            return channel.receive()
        }

    }

}
