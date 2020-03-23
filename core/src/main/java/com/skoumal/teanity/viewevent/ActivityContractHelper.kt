package com.skoumal.teanity.viewevent

import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.skoumal.teanity.viewevent.base.ActivityExecutor
import com.skoumal.teanity.viewevent.base.ViewEvent

typealias ActivityContractHelperListener<T> = (T) -> Unit

class ActivityContractHelper<In, Out>(
    private val contract: ActivityResultContract<In, Out>,
    private val input: In,
    private val listener: ActivityContractHelperListener<Out>
) : ViewEvent(), ActivityExecutor {

    override fun invoke(activity: AppCompatActivity) {
        activity.prepareCall(contract) {
            listener(it)
        }.launch(input)
    }

}