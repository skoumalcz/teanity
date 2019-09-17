package com.skoumal.teanity.view

import com.skoumal.teanity.viewmodel.TeanityViewModel

internal interface TeanityViewAccessor<ViewModel : TeanityViewModel> {

    fun obtainViewModel(): ViewModel
    fun obtainLayoutRes(): Int

}