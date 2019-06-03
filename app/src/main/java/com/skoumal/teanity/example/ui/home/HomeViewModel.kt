package com.skoumal.teanity.example.ui.home

import com.skoumal.teanity.BR
import com.skoumal.teanity.api.Result
import com.skoumal.teanity.api.map
import com.skoumal.teanity.databinding.GenericRvItem
import com.skoumal.teanity.example.data.repository.PhotoRepository
import com.skoumal.teanity.example.model.entity.inbound.Photo
import com.skoumal.teanity.example.model.entity.recycler.LoadingRvItem
import com.skoumal.teanity.example.model.entity.recycler.PhotoRvItem
import com.skoumal.teanity.example.model.navigation.Navigation
import com.skoumal.teanity.extensions.bindingOf
import com.skoumal.teanity.extensions.diffListOf
import com.skoumal.teanity.viewmodel.LoadingViewModel

class HomeViewModel(
    private val photoRepository: PhotoRepository
) : LoadingViewModel() {

    val items = diffListOf<GenericRvItem>()
    val itemBinding = bindingOf<GenericRvItem> {
        it.bindExtra(BR.viewModel, this@HomeViewModel)
    }

    private val photoItems get() = items.filterIsInstance<PhotoRvItem>()
    private val currentLoadingItem get() = items.filterIsInstance<LoadingRvItem>().firstOrNull()
    private val loadingItem
        get() = LoadingRvItem(
            "Loading failed",
            "Try again",
            ::loadMoreItems
        )

    init {
        loadItems()
    }

    fun retryLoadingButtonClicked() {
        loadItems()
    }

    private fun loadItems() = launch {
        if (photoItems.isEmpty()) {
            state = State.LOADING
        } else {
            currentLoadingItem?.failed?.set(false)
        }

        // thread suspended here, waiting for photos to be fetched
        val photos = photoRepository.getPhotos(offset = photoItems.size).map { PhotoRvItem(it) }
        // back in execution

        onItemsLoaded(photos)
    }

    //region loadItems()
    private fun onItemsLoaded(it: Result<List<PhotoRvItem>>): Unit = when (it) {
        is Result.Success -> {
            state = State.LOADED
            items.updateAsync(items + it.data)
        }
        is Result.Error -> {
            if (photoItems.isEmpty()) {
                state = State.LOADING_FAILED
            } else {
                currentLoadingItem?.failed?.set(true) ?: Unit
            }
        }
        is Result.Void -> {
            state = State.LOADED
        }
    }
    //endregion

    fun loadMoreItems() {
        loadItems()
    }

    fun photoClicked(photo: Photo) = Navigation.photo(photo).publish()
}