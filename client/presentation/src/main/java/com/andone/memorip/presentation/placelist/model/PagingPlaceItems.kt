package com.andone.memorip.presentation.placelist.model

import androidx.paging.compose.LazyPagingItems
import com.andone.memorip.presentation.model.Place

class PagingPlaceItems(
    private val items: LazyPagingItems<Place>
) : PlaceItems {

    override val itemCount: Int
        get() = items.itemCount

    override fun get(index: Int): Place? =
        items[index]
}
