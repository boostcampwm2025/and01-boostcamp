package com.andone.memorip.presentation.placelist.model

import androidx.paging.compose.LazyPagingItems

class PagingPlaceItems(
    private val items: LazyPagingItems<PlaceListUiItem>
) : PlaceItems {

    override val itemCount: Int
        get() = items.itemCount

    override fun get(index: Int): PlaceListUiItem? =
        items[index]
}
