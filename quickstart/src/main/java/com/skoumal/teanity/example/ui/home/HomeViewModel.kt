package com.skoumal.teanity.example.ui.home

import com.skoumal.teanity.BR
import com.skoumal.teanity.databinding.ComparableRvItem
import com.skoumal.teanity.util.DiffObservableList
import com.skoumal.teanity.viewmodel.LoadingViewModel
import me.tatarka.bindingcollectionadapter2.OnItemBind

class HomeViewModel : LoadingViewModel() {

    val items = DiffObservableList(ComparableRvItem.callback)
    val itemBinding = OnItemBind<ComparableRvItem<*>> { itemBinding, _, item ->
        item.bind(itemBinding)
        itemBinding.bindExtra(BR.viewModel, this@HomeViewModel)
    }

    init {
    }

    fun retryLoadingButtonClicked() {
    }

    fun loadMoreItems() {
    }
}