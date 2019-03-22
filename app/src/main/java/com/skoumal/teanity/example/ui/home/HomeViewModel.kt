package com.skoumal.teanity.example.ui.home

import com.skoumal.teanity.BR
import com.skoumal.teanity.api.IApiX
import com.skoumal.teanity.databinding.ComparableRvItem
import com.skoumal.teanity.example.data.repository.PhotoRepository
import com.skoumal.teanity.example.model.base.ExampleViewModel
import com.skoumal.teanity.example.model.entity.LoadingRvItem
import com.skoumal.teanity.example.model.entity.Photo
import com.skoumal.teanity.example.model.entity.PhotoRvItem
import com.skoumal.teanity.extensions.applySchedulers
import com.skoumal.teanity.util.DiffObservableList
import me.tatarka.bindingcollectionadapter2.OnItemBind
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val photoRepository: PhotoRepository
) : ExampleViewModel() {

    val items = DiffObservableList(ComparableRvItem.callback)
    val itemBinding = OnItemBind<ComparableRvItem<*>> { itemBinding, _, item ->
        item.bind(itemBinding)
        itemBinding.bindExtra(BR.viewModel, this@HomeViewModel)
    }

    private val photoItems get() = items.filterIsInstance<PhotoRvItem>()
    private val currentLoadingItem get() = items.filterIsInstance<LoadingRvItem>().firstOrNull()
    private val loadingItem get() = LoadingRvItem("Loading failed", "Try again", ::loadMoreItems)

    init {
        loadItems()
    }

    fun retryLoadingButtonClicked() {
        loadItems()
    }

    private fun loadItems() {
        photoRepository.getPhotos { offset = photoItems.size }
            .flattenAsFlowable { it }
            .map { PhotoRvItem(it) }
            .toList()
            .delay(1000, TimeUnit.MILLISECONDS)
            .applySchedulers()
            .doOnSubscribe {
                if (photoItems.isEmpty()) {
                    state = State.LOADING
                } else {
                    currentLoadingItem?.failed?.set(false)
                }
            }
            .subscribe({
                state = State.LOADED
                itemsLoaded(it)
            }, {
                if (photoItems.isEmpty()) {
                    state = State.LOADING_FAILED
                } else {
                    currentLoadingItem?.failed?.set(true)
                }
            })
            .add()
    }

    fun loadMoreItems() = loadItems()

    private fun itemsLoaded(newItems: List<PhotoRvItem>) {
        val updatedList = photoItems + newItems

        items.update(updatedList.plusNotEmpty(loadingItem))
    }

    fun photoClicked(photo: Photo) = photoDetail(photo)

    private fun List<ComparableRvItem<*>>.plusNotEmpty(item: ComparableRvItem<*>): List<ComparableRvItem<*>> {
        return if (size % IApiX.GENERIC_LIMIT == 0) this + item
        else this
    }
}