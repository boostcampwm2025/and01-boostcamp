package com.andone.memorip.presentation.placelist.model

import com.andone.memorip.presentation.model.Place

interface PlaceItems {
    val itemCount: Int
    operator fun get(index: Int): Place?
}
