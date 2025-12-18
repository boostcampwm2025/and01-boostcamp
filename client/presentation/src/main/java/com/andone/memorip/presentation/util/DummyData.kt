package com.andone.memorip.presentation.util

import com.andone.memorip.presentation.model.ImageItem
import com.andone.memorip.presentation.model.Place
import kotlin.random.Random

object DummyData {
    private const val IMAGES_PER_PLACE = 50
    
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
        val images = List(IMAGES_PER_PLACE) { imageIndex ->
            val photoId = Random.nextInt(30, 81)
            val randomHeight = Random.nextInt(150, 400)
            val fixedWidth = 200
            
            ImageItem(
                id = placeIndex * IMAGES_PER_PLACE + imageIndex,
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
}