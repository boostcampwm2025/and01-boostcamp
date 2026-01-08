package com.andone.memorip.presentation.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.andone.memorip.domain.model.Tag
import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.model.ImageItem
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.placedetail.model.PlaceUiModel
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

object DummyData {

    val place = PlaceUiModel(
        title = "제목",
        tags = persistentListOf(
            Tag(
                id = UUID.randomUUID().toString(),
                name = "맛집",
                color = "#${Integer.toHexString(Color.Gray.toArgb())}"
            ).toUiModel()
        ),
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

    val categories = mutableStateListOf(
        TagUiModel(
            id = UUID.randomUUID().toString(),
            name = "맛집",
            color = Color(0xFF000000)
        ),
        TagUiModel(
            id = UUID.randomUUID().toString(),
            name = "카페",
            color = Color(0xFAA8F0F0)
        ),
        TagUiModel(
            id = UUID.randomUUID().toString(),
            name = "액티비티",
            color = Color(0xFFCCDD66)
        ),
    )

    val places: List<Place> by lazy {
        buildList {
            add(
                createPlace(
                    "",
                    "브런치 카페",
                    37.498095,
                    127.027610,
                    "강남구, 서울",
                    LocalDateTime.of(2025, 12, 1, 9, 0),
                    LocalDateTime.of(2025, 12, 1, 9, 30),
                    0
                )
            )
            add(
                createPlace(
                    "",
                    "예쁜 공원",
                    37.512900,
                    127.058500,
                    "성동구, 서울",
                    LocalDateTime.of(2025, 12, 1, 10, 0),
                    LocalDateTime.of(2025, 12, 1, 14, 0),
                    1
                )
            )
            add(
                createPlace(
                    "",
                    "야경 맛집",
                    37.517305,
                    127.047502,
                    "성수동, 서울",
                    LocalDateTime.of(2025, 12, 1, 18, 0),
                    LocalDateTime.of(2025, 12, 1, 20, 0),
                    2
                )
            )
            add(
                createPlace(
                    "",
                    "루프탑 바",
                    37.505228,
                    127.050324,
                    "왕십리, 서울",
                    LocalDateTime.of(2025, 12, 1, 20, 30),
                    LocalDateTime.of(2025, 12, 1, 23, 0),
                    0
                )
            )
            add(
                createPlace(
                    "",
                    "숨은 카페",
                    37.508547,
                    127.062835,
                    "성수동, 서울",
                    LocalDateTime.of(2025, 12, 1, 14, 0),
                    LocalDateTime.of(2025, 12, 1, 16, 0),
                    1
                )
            )
            add(
                createPlace(
                    "",
                    "감성 서점",
                    37.495592,
                    127.028747,
                    "강남구, 서울",
                    LocalDateTime.of(2025, 12, 1, 13, 0),
                    LocalDateTime.of(2025, 12, 1, 15, 30),
                    2
                )
            )
        }
    }

    private fun createPlaceImages(placeId: String, count: Int = 50): List<ImageItem> {
        return List(count) { imageIndex ->
            val photoId = Random.nextInt(30, 81)
            val randomHeight = Random.nextInt(150, 400)
            val fixedWidth = 200

            ImageItem(
                id = count + imageIndex,
                url = "https://picsum.photos/id/$photoId/$fixedWidth/$randomHeight",
                width = fixedWidth,
                height = randomHeight
            )
        }
    }

    private fun createPlace(
        id: String,
        name: String,
        latitude: Double,
        longitude: Double,
        address: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        categoryIndex: Int
    ): Place {
        val images = createPlaceImages(id)

        return Place(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude,
            address = address,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            categories = categories,
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