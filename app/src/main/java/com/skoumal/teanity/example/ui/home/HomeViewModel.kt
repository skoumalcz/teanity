package com.skoumal.teanity.example.ui.home

import com.skoumal.teanity.BR
import com.skoumal.teanity.api.ApiX
import com.skoumal.teanity.api.Result
import com.skoumal.teanity.api.map
import com.skoumal.teanity.databinding.ComparableRvItem
import com.skoumal.teanity.example.data.repository.PhotoRepository
import com.skoumal.teanity.example.model.base.ExampleViewModel
import com.skoumal.teanity.example.model.entity.LoadingRvItem
import com.skoumal.teanity.example.model.entity.Photo
import com.skoumal.teanity.example.model.entity.PhotoRvItem
import com.skoumal.teanity.util.DiffObservableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.OnItemBind

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

    private fun loadItems() = network<List<PhotoRvItem>> {
        onStart(::onStartLoadItems)
        onProcess(::onProcessLoadItems)
        onFinished(::onFinishedLoadItems)
    }

    //region loadItems()
    private fun onStartLoadItems() {
        if (photoItems.isEmpty()) {
            state = State.LOADING
        } else {
            currentLoadingItem?.failed?.set(false)
        }
    }

    private suspend fun onProcessLoadItems() = withContext(Dispatchers.IO) {
        delay(1000)
        photoRepository.getPhotos { offset = photoItems.size }.map { it.map { PhotoRvItem(it) } }
    }

    private fun onFinishedLoadItems(it: Result<List<PhotoRvItem>>): Unit = when (it) {
        is Result.Success -> {
            state = State.LOADED
            itemsLoaded(it.data)
        }
        is Result.Error -> {
            if (photoItems.isEmpty()) {
                state = State.LOADING_FAILED
            } else {
                currentLoadingItem?.failed?.set(true) ?: Unit
            }
        }
    }
    //endregion

    fun loadMoreItems() {
        loadItems()
    }

    private fun itemsLoaded(newItems: List<PhotoRvItem>) {
        val updatedList = photoItems + newItems

        items.update(updatedList.plusNotEmpty(loadingItem))
    }

    fun photoClicked(photo: Photo) = photoDetail(photo)

    private fun List<ComparableRvItem<*>>.plusNotEmpty(item: ComparableRvItem<*>): List<ComparableRvItem<*>> {
        return if (size % ApiX.GENERIC_LIMIT == 0) this + item
        else this
    }
}