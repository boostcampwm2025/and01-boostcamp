package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import java.time.Duration
import java.time.LocalDateTime

@Immutable
data class Place(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val categories: List<Category>,
    val thumbnailImage: ImageItem,
    val images: List<ImageItem>
) {
    val durationMinutes: Long
        get() = Duration.between(startDateTime, endDateTime).toMinutes()

    val isMultiDay: Boolean
        get() = startDateTime.toLocalDate() != endDateTime.toLocalDate()

    companion object {
        fun empty(): Place = Place(
            id = 0,
            name = "",
            latitude = 0.0,
            longitude = 0.0,
            thumbnailImage = ImageItem.empty(),
            images = emptyList()
        )
    }
}