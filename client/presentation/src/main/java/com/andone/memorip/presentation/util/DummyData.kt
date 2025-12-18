package com.andone.memorip.presentation.util

import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.placedetail.model.PlaceDetail
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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

    val place = PlaceDetail(
        title = "제목",
        category = "맛집",
        locationName = "서울시 종로구",
        imageUrls = persistentListOf(
            "https://picsum.photos/200/50",
            "https://picsum.photos/200/100",
            "https://picsum.photos/200/200",
            "https://picsum.photos/200/400"
        ),
        groupName = "크리스마스",
        content = "test content, test content, test content, test content\n test content, test content1\n test content, test content2\n test content, test content3\n test content, test content4\n test content, test content5\n test content, test content6\n test content, test content7\n test content, test content8\n test content, test content9\n test content, test content10\n test content, test content11\n test content, test content12\n test content, test content13\n test content, test content14\n test content, test content15"
    )

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