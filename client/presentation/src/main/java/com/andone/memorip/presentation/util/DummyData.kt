package com.andone.memorip.presentation.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.andone.memorip.presentation.groupdetail.model.PlaceImageItem
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.placedetail.model.PlaceDetail
import com.andone.memorip.presentation.selectcategory.model.Category
import kotlinx.collections.immutable.persistentListOf

object DummyData {

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

    val categories = mutableStateListOf(
        Category(
            id = 0L,
            category = "맛집",
            color = Color(0xFF000000)
        ),
        Category(
            id = 1L,
            category = "카페",
            color = Color(0xFAA8F0F0)
        ),
        Category(
            id = 2L,
            category = "액티비티",
            color = Color(0xFFCCDD66)
        ),
    )

    val groups = mutableStateListOf(
        GroupUiModel(
            name = "기본 그룹",
            images = List(size = 8) { "" }
        ),
        GroupUiModel(
            name = "부 산",
            images = List(size = 4) { "" }
        ),
        GroupUiModel(
            name = "제주도",
            images = List(size = 5) { "" }
        ),
        GroupUiModel(
            name = "대구 ",
            images = List(size = 1) { "" }
        )
    )
}