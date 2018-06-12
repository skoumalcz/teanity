package com.skoumal.teanity.model.entity

import android.databinding.ObservableBoolean
import android.support.annotation.CallSuper
import com.skoumal.teanity.BR
import com.skoumal.teanity.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

abstract class RvItem {

    abstract val layoutRes: Int

    @CallSuper
    open fun bind(binding: ItemBinding<*>) {
        binding.set(BR.item, layoutRes)
    }
}

abstract class ComparableRvItem : RvItem() {

    abstract fun itemSameAs(other: ComparableRvItem): Boolean
    abstract fun contentSameAs(other: ComparableRvItem): Boolean
}

class PhotoRvItem(val photo: Photo) : ComparableRvItem() {

    override val layoutRes = R.layout.item_photo

    override fun itemSameAs(other: ComparableRvItem): Boolean {
        return other is PhotoRvItem && photo.id == other.photo.id
    }

    override fun contentSameAs(other: ComparableRvItem): Boolean {
        return other is PhotoRvItem && photo == other.photo
    }
}

class LoadingRvItem(
        val failText: String,
        val failActionText: String,
        private val failAction: () -> Unit,
        isFailed: Boolean = false
) : ComparableRvItem() {

    override val layoutRes = R.layout.item_loading_more

    val failed = ObservableBoolean(isFailed)

    fun failActionClicked() = failAction()

    override fun itemSameAs(other: ComparableRvItem) = other is LoadingRvItem

    override fun contentSameAs(other: ComparableRvItem) = true
}