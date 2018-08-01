package com.skoumal.teanity.example.model.entity

import androidx.databinding.ObservableBoolean
import com.skoumal.teanity.databinding.ComparableRvItem
import com.skoumal.teanity.example.R
import com.skoumal.teanity.util.ComparableCallback

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