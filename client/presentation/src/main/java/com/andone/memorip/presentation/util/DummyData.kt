package com.andone.memorip.presentation.util

import com.andone.memorip.presentation.groupdetail.model.PlaceImageItem

object DummyData {
    val dummyImages = List(30) { index ->
        val randomHeight = (150..400).random()
        val fixedWidth = 200
        PlaceImageItem(
            id = index,
            url = "https://picsum.photos/id/${index + 1}/$fixedWidth/$randomHeight",
            width = fixedWidth,
            height = randomHeight
        )
    }
}