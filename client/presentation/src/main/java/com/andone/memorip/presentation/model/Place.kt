package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.PlaceListItem
import java.time.Duration
import java.time.LocalDateTime

@Immutable
data class Place(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val categories: List<TagUiModel>,
    val thumbnailImage: ImageItem,
    val images: List<ImageItem>
) {
    val durationMinutes: Long
        get() = Duration.between(startDateTime, endDateTime).toMinutes()

    val isMultiDay: Boolean
        get() = startDateTime.toLocalDate() != endDateTime.toLocalDate()

    companion object {
        fun empty(): Place = Place(
            id = "",
            name = "",
            latitude = 0.0,
            longitude = 0.0,
            address = "",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now(),
            categories = emptyList(),
            thumbnailImage = ImageItem.empty(),
            images = emptyList()
        )
    }
}

fun PlaceListItem.toUiModel(): Place =
    Place(
        id = id,
        name = title,
        latitude = latitude,
        longitude = longitude,
        address = address,
        startDateTime = LocalDateTime.now(),
        endDateTime = LocalDateTime.now(),
        categories = emptyList(),
        thumbnailImage = ImageItem(
            id = 0,
            url = imageUrl,
            width = 1,
            height = 1
        ),
        images = emptyList()
    )