package com.skoumal.teanity.model.entity

import android.databinding.ObservableBoolean
import android.support.annotation.CallSuper
import com.skoumal.teanity.BR
import com.skoumal.teanity.R
import com.skoumal.teanity.util.ComparableCallback
import com.skoumal.teanity.util.DiffObservableList
import me.tatarka.bindingcollectionadapter2.ItemBinding

abstract class RvItem {

    abstract val layoutRes: Int

    @CallSuper
    open fun bind(binding: ItemBinding<*>) {
        binding.set(BR.item, layoutRes)
    }
}

abstract class ComparableRvItem<in T> : RvItem() {

    abstract fun itemSameAs(other: T): Boolean
    abstract fun contentSameAs(other: T): Boolean

    companion object {
        fun callback(
            areItemsTheSame: (ComparableRvItem<*>, ComparableRvItem<*>) -> Boolean,
            areContentsTheSame: (ComparableRvItem<*>, ComparableRvItem<*>) -> Boolean
        ) = object : DiffObservableList.Callback<ComparableRvItem<*>> {
            override fun areItemsTheSame(
                oldItem: ComparableRvItem<*>,
                newItem: ComparableRvItem<*>
            ) = when {
                oldItem::class == newItem::class -> areItemsTheSame(oldItem, newItem)
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: ComparableRvItem<*>,
                newItem: ComparableRvItem<*>
            ) = when {
                oldItem::class == newItem::class -> areContentsTheSame(oldItem, newItem)
                else -> false
            }
        }
    }
}

class PhotoRvItem(val photo: Photo) : ComparableRvItem<PhotoRvItem>() {

    override val layoutRes = R.layout.item_photo

    override fun itemSameAs(other: PhotoRvItem): Boolean {
        return photo.sameAs(other.photo)
    }

    override fun contentSameAs(other: PhotoRvItem): Boolean {
        return photo.contentSameAs(other.photo)
    }

    companion object : ComparableCallback<PhotoRvItem>()
}

class LoadingRvItem(
    val failText: String,
    val failActionText: String,
    private val failAction: () -> Unit,
    isFailed: Boolean = false
) : ComparableRvItem<LoadingRvItem>() {

    override val layoutRes = R.layout.item_loading_more

    val failed = ObservableBoolean(isFailed)

    fun failActionClicked() = failAction()

    override fun itemSameAs(other: LoadingRvItem) = true

    override fun contentSameAs(other: LoadingRvItem) = true

    companion object : ComparableCallback<PhotoRvItem>()
}