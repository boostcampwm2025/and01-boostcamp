package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.TimeBlock
import java.time.Duration
import java.time.LocalDateTime

@Immutable
data class Place(
    override val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val startDateTime: LocalDateTime?,
    val endDateTime: LocalDateTime?,
    val categories: List<TagUiModel>,
    val thumbnailImage: ImageItem,
    val images: List<ImageItem>
) : PlanBlockUiModel {
    val durationMinutes: Long
        get() = if (startDateTime != null && endDateTime != null)
            Duration.between(startDateTime, endDateTime).toMinutes()
        else 0L


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

fun Place.toTimeBlock(dayStart: LocalDateTime): TimeBlock? {
    if (startDateTime == null || endDateTime == null) return null

    val dayOffset = Duration.between(
        dayStart.toLocalDate().atStartOfDay(),
        startDateTime.toLocalDate().atStartOfDay()
    ).toDays().toInt()

    val dayIndex = dayOffset + 1

    val startMinute =
        Duration.between(dayStart, startDateTime).toMinutes().toInt()

    val durationMinute = durationMinutes.toInt()

    return TimeBlock(
        id = id,
        startMinute = startMinute,
        durationMinute = durationMinute,
        day = dayIndex,
        column = 0
    )
}
