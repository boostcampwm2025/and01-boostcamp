package com.andone.memorip.presentation.placelist.model

import com.andone.memorip.presentation.model.Place

class ListPlaceItems(private val items: List<Place>) : PlaceItems {

    override val itemCount: Int
        get() = items.size

    override fun get(index: Int): Place? =
        items[index]
}
