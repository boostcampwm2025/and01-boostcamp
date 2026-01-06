package com.andone.memorip.presentation.placelist.model

interface PlaceItems {
    val itemCount: Int
    operator fun get(index: Int): PlaceListUiItem?
}
