package com.skoumal.teanity.example.model.entity

import androidx.databinding.ObservableBoolean
import com.skoumal.teanity.databinding.GenericRvItem
import com.skoumal.teanity.example.R
import com.skoumal.teanity.extensions.compareToSafe

class PhotoRvItem(val photo: Photo) : GenericRvItem() {

    override val layoutRes = R.layout.item_photo

    override fun sameAs(other: GenericRvItem) =
        other.compareToSafe<PhotoRvItem> { photo.sameAs(it.photo) }

    override fun contentSameAs(other: GenericRvItem) =
        other.compareToSafe<PhotoRvItem> { photo.contentSameAs(it.photo) }
}

class LoadingRvItem(
    val failText: String,
    val failActionText: String,
    private val failAction: () -> Unit,
    isFailed: Boolean = false
) : GenericRvItem() {

    override val layoutRes = R.layout.item_loading_more

    val failed = ObservableBoolean(isFailed)

    fun failActionClicked() = failAction()

    override fun sameAs(other: GenericRvItem) = other.compareToSafe<LoadingRvItem> { true }
    override fun contentSameAs(other: GenericRvItem) = other.compareToSafe<LoadingRvItem> { true }
}