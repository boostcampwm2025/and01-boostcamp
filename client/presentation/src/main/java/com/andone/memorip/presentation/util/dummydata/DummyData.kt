package com.andone.memorip.presentation.util.dummydata

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.selectcategory.model.Category

object DummyData {

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