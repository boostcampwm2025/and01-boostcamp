package com.andone.memorip.presentation.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.andone.memorip.presentation.placelist.model.GroupUiModel
import com.andone.memorip.presentation.model.ImageItem
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.selectcategory.model.Category
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random

object DummyData {

    val place = PlaceUiModel(
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

    val placeImages: List<ImageItem> = List(12) { index ->
        ImageItem(
            id = index + 1,
            url = "https://picsum.photos/seed/${index + 1}/800/800",
            width = 800,
            height = 800
        )
    }

    val places: List<Place> = listOf(
        Triple(37.498095, 127.027610, "브런치 카페"),
        Triple(37.512900, 127.058500, "예쁜 공원"),
        Triple(37.517305, 127.047502, "야경 맛집"),
        Triple(37.505228, 127.050324, "루프탑 바"),
        Triple(37.508547, 127.062835, "숨은 카페"),
        Triple(37.495592, 127.028747, "감성 서점")
    ).mapIndexed { placeIndex, (lat, lng, placeName) ->
        val images = List(50) { imageIndex ->
            val photoId = Random.nextInt(30, 81)
            val randomHeight = Random.nextInt(150, 400)
            val fixedWidth = 200

            ImageItem(
                id = placeIndex * 50 + imageIndex,
                url = "https://picsum.photos/id/$photoId/$fixedWidth/$randomHeight",
                width = fixedWidth,
                height = randomHeight
            )
        }

        Place(
            id = placeIndex,
            name = placeName,
            latitude = lat,
            longitude = lng,
            thumbnailImage = images.first(),
            images = images
        )
    }

    val imageItems = (30..80).map { id ->
        val randomHeight = (50..400).random()
        val fixedWidth = 200

        ImageItem(
            id = id,
            url = "https://picsum.photos/id/$id/$fixedWidth/$randomHeight",
            width = fixedWidth,
            height = randomHeight
        )
    }

    val groupName = "Group1"
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
            images = createRandomImageUrls(8, "group1")
        ),
        GroupUiModel(
            name = "부 산",
            images = createRandomImageUrls(4, "busan")
        ),
        GroupUiModel(
            name = "제주도",
            images = createRandomImageUrls(5, "jeju")
        ),
        GroupUiModel(
            name = "대구 ",
            images = createRandomImageUrls(1, "daegu")
        )
    )

    private fun createRandomImageUrls(count: Int, seedKey: String): List<String> {
        return List(count) { index ->
            val height = Random.nextInt(200, 400)
            "https://picsum.photos/seed/${seedKey}_$index/200/$height"
        }
    }
}