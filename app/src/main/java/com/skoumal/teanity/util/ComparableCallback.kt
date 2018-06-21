package com.skoumal.teanity.util

import com.skoumal.teanity.model.entity.ComparableRvItem

open class ComparableCallback<Comparable : ComparableRvItem<Comparable>> {
    val callback = object : DiffObservableList.Callback<Comparable> {
        override fun areItemsTheSame(oldItem: Comparable, newItem: Comparable) =
            oldItem.itemSameAs(newItem)

        override fun areContentsTheSame(oldItem: Comparable, newItem: Comparable) =
            oldItem.contentSameAs(newItem)
    }
}