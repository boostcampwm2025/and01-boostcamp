package com.andone.memorip.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.domain.model.Tag
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.model.ImageItem
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.placedetail.model.TripCompactUiModel
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.screen.placelist.model.RegionUiModel
import com.andone.memorip.presentation.screen.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.screen.plan.model.DateUiModel
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.model.SettingItemUiModel
import com.andone.memorip.presentation.screen.user.model.SettingTrailing
import com.andone.memorip.presentation.screen.user.model.UserUiModel
import com.andone.memorip.presentation.screen.plan.model.TripListUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
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
        trips = persistentListOf(
            TripCompactUiModel(
                tripId = UUID.randomUUID().toString(),
                tripName = "크리스마스"
            )
        ),
        content = "test content, test content, test content, test content\n test content, test content1\n test content, test content2\n test content, test content3\n test content, test content4\n test content, test content5\n test content, test content6\n test content, test content7\n test content, test content8\n test content, test content9\n test content, test content10\n test content, test content11\n test content, test content12\n test content, test content13\n test content, test content14\n test content, test content15"
    )

    val placeImages: List<ImageItem> = List(12) { index ->
        ImageItem(
            id = index + 1,
            url = "https://picsum.photos/seed/${index + 1}/800/800",
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

    val regions: Set<RegionUiModel> = setOf(
        RegionUiModel(id = "1", name = "서울", isSelected = true),
        RegionUiModel(id = "2", name = "경기"),
        RegionUiModel(id = "3", name = "인천"),
        RegionUiModel(id = "4", name = "강원"),
        RegionUiModel(id = "5", name = "충청북도"),
        RegionUiModel(id = "6", name = "충청남도"),
        RegionUiModel(id = "7", name = "전라북도"),
        RegionUiModel(id = "8", name = "전라남도", isSelected = true),
        RegionUiModel(id = "9", name = "경상북도"),
        RegionUiModel(id = "10", name = "경상남도"),
        RegionUiModel(id = "11", name = "대전"),
        RegionUiModel(id = "12", name = "세종"),
        RegionUiModel(id = "13", name = "대구"),
        RegionUiModel(id = "14", name = "부산"),
        RegionUiModel(id = "15", name = "울산"),
        RegionUiModel(id = "16", name = "광주"),
        RegionUiModel(id = "17", name = "제주특별자치도")
    )

    val regionState: SelectedRegionState = SelectedRegionState(
        parents = listOf(
            RegionUiModel(id = "seoul", name = "서울", level = 0),
            RegionUiModel(id = "gangnam", name = "강남구", level = 1)
        ),
        child = setOf(
            RegionUiModel(id = "yeoksam", name = "역삼동", level = 2),
            RegionUiModel(id = "samseong", name = "삼성동", level = 2)
        )
    )

    val places: List<Place> by lazy {
        buildList {
            add(
                createPlace(
                    "0",
                    "브런치 카페",
                    37.498095,
                    127.027610,
                    "강남구, 서울",
                    LocalDateTime.of(2025, 12, 1, 9, 0),
                    LocalDateTime.of(2025, 12, 1, 10, 0),
                    0
                )
            )
            add(
                createPlace(
                    "1",
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
                    "2",
                    "야경 맛집",
                    37.517305,
                    127.047502,
                    "성수동, 서울",
                    LocalDateTime.of(2025, 12, 1, 19, 0),
                    LocalDateTime.of(2025, 12, 1, 21, 0),
                    2
                )
            )
            add(
                createPlace(
                    "3",
                    "루프탑 바",
                    37.505228,
                    127.050324,
                    "왕십리, 서울",
                    LocalDateTime.of(2025, 12, 1, 21, 0),
                    LocalDateTime.of(2025, 12, 1, 23, 30),
                    0
                )
            )
            add(
                createPlace(
                    "4",
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
                    "5",
                    "감성 서점",
                    37.495592,
                    127.028747,
                    "강남구, 서울",
                    LocalDateTime.of(2025, 12, 1, 16, 0),
                    LocalDateTime.of(2025, 12, 1, 18, 30),
                    2
                )
            )
            add(
                createPlace(
                    "6",
                    "KFCKFCKFCKFCKFCKFCKFCKFCKFCKFCKFCKFCKFCKFC",
                    37.495592,
                    127.028747,
                    "경기도, 광명",
                    LocalDateTime.of(2025, 12, 2, 13, 0),
                    LocalDateTime.of(2025, 12, 2, 15, 30),
                    1
                )
            )
        }
    }

    val dummyUser = UserUiModel(
        name = "홍길동",
        id = "0",
        profileImgUrl = "",
        email = "asdfgqwe@naver.com"
    )

    val permissionItems = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_baseline_camera_alt,
            title = "카메라",
            subtitle = "프로필 사진 변경",
            trailing = SettingTrailing.Arrow(isAllowed = true),
            onClick = { }
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_gallery_thumbnail,
            title = "사진 갤러리",
            subtitle = "이미지 업로드",
            trailing = SettingTrailing.Arrow(isAllowed = true),
            onClick = { }
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_location_on,
            title = "위치",
            subtitle = "위치 기반 서비스",
            trailing = SettingTrailing.Arrow(),
            onClick = { }
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_android_wifi_3_bar,
            title = "네트워크",
            subtitle = "네트워크 상태 변경 알림",
            trailing = SettingTrailing.Arrow()
        )
    )

    val dummyDate =
        DateUiModel(
            startDay = LocalDate.of(2025, 12, 1),
            endDay = LocalDate.of(2025, 12, 2),
            currentDay = LocalDate.of(2025, 12, 1)
        )

    private fun createPlaceImages(placeId: String, count: Int = 50): List<ImageItem> {
        return List(count) { imageIndex ->
            val photoId = Random.nextInt(30, 81)
            val randomHeight = Random.nextInt(150, 400)
            val fixedWidth = 200

            ImageItem(
                id = count + imageIndex,
                url = "https://picsum.photos/id/$photoId/$fixedWidth/$randomHeight",
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
            id = "",
            placeId = id,
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
        )
    }

    val groupName = "Group1"

    val tripListItems = mutableStateListOf(
        TripListUiModel(
            id = UUID.randomUUID().toString(),
            title = "기본 그룹"
        ),
        TripListUiModel(
            id = UUID.randomUUID().toString(),
            title = "부산"
        ),
        TripListUiModel(
            id = UUID.randomUUID().toString(),
            title = "제주도"
        ),
        TripListUiModel(
            id = UUID.randomUUID().toString(),
            title = "대구"
        )
    )

    val trips = mutableStateListOf(
        TripUiModel(
            id = UUID.randomUUID().toString(),
            name = "기본 그룹",
            images = createRandomImageUrls(8, "group1")
        ),
        TripUiModel(
            id = UUID.randomUUID().toString(),
            name = "부 산",
            images = createRandomImageUrls(4, "busan")
        ),
        TripUiModel(
            id = UUID.randomUUID().toString(),
            name = "제주도",
            images = createRandomImageUrls(5, "jeju")
        ),
        TripUiModel(
            id = UUID.randomUUID().toString(),
            name = "대구 ",
            images = createRandomImageUrls(1, "daegu")
        )
    )

    val locations: List<LocationUiModel> by lazy {
        listOf(
            LocationUiModel(
                id = "1",
                name = "스타벅스 강남R점",
                category = "카페",
                address = "서울특별시 강남구 역삼동 825-2",
                roadAddress = "서울특별시 강남구 강남대로 390",
                latitude = 37.4979,
                longitude = 127.0276
            ),
            LocationUiModel(
                id = "2",
                name = "코엑스",
                category = "복합문화공간",
                address = "서울특별시 강남구 삼성동 159",
                roadAddress = "서울특별시 강남구 영동대로 513",
                latitude = 37.5118,
                longitude = 127.0593
            ),
            LocationUiModel(
                id = "3",
                name = "남산서울타워",
                category = "관광명소",
                address = "서울특별시 용산구 용산동2가 산 1-3",
                roadAddress = "서울특별시 용산구 남산공원길 105",
                latitude = 37.5511,
                longitude = 126.9882
            ),
            LocationUiModel(
                id = "4",
                name = "뚝섬한강공원",
                category = "공원",
                address = "서울특별시 광진구 자양동 427-6",
                roadAddress = "서울특별시 광진구 강변북로 139",
                latitude = 37.5294,
                longitude = 127.0739
            ),
            LocationUiModel(
                id = "5",
                name = "롯데월드",
                category = "테마파크",
                address = "서울특별시 송파구 잠실동 40-1",
                roadAddress = "서울특별시 송파구 올림픽로 240",
                latitude = 37.5111,
                longitude = 127.0981
            )
        )
    }

    val timeBlocks = listOf(
        TimeBlock("0", 0, 60),
        TimeBlock("1", 9 * 60, 60),
        TimeBlock("2", 11 * 60 + 30, 90),
        TimeBlock("3", 15 * 60, 45)
    )

    @Composable
    fun getPlacePagingItems(): LazyPagingItems<Place> {
        return flowOf(PagingData.from(places)).collectAsLazyPagingItems()
    }

    @Composable
    fun getTagPagingItems(): LazyPagingItems<TagUiModel> {
        return flowOf(PagingData.from(categories)).collectAsLazyPagingItems()
    }

    private fun createRandomImageUrls(count: Int, seedKey: String): List<String> {
        return List(count) { index ->
            val height = Random.nextInt(200, 400)
            "https://picsum.photos/seed/${seedKey}_$index/200/$height"
        }
    }
}