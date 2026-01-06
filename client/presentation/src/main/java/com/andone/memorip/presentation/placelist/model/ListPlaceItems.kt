package com.andone.memorip.presentation.placelist.model

class ListPlaceItems(
    private val items: List<PlaceListUiItem>
) : PlaceItems {

    override val itemCount: Int
        get() = items.size

    override fun get(index: Int): PlaceListUiItem? =
        items[index]
}
