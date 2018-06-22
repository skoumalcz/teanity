package com.skoumal.teanity.databinding

import android.databinding.BaseObservable
import android.databinding.Bindable

/**
 * We need binding processor to generate BR.item & BR.viewModel,
 * which are used in [RvItem], [com.skoumal.teanity.view.TeanityActivity]
 * and [com.skoumal.teanity.view.TeanityFragment].
 * You must define those in your layouts.
 */
object BindingResources : BaseObservable() {

    val item @Bindable get() = 0
    val viewModel @Bindable get() = 0
}