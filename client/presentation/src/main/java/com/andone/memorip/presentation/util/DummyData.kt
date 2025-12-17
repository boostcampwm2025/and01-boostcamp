package com.andone.memorip.presentation.util

import com.andone.memorip.presentation.home.model.GroupUiModel

object DummyData {

    val randomImages = List(28) { index ->
        val height = (100..400).random()
        "https://picsum.photos/id/$index/200/$height"
    }

    val groups = randomImages
        .chunked(7)
        .mapIndexed { index, images ->
            GroupUiModel(
                name = index.toString(),
                images = images
            )
        }
}