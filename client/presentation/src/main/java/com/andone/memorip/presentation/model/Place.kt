package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.TripPlace
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Immutable
data class Place(
    override val id: String,
    val placeId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    override val startDateTime: LocalDateTime?,
    override val endDateTime: LocalDateTime?,
    val categories: List<TagUiModel>,
    val thumbnailImage: ImageItem,
    val images: List<ImageItem>
) : PlanBlockUiModel {
    val durationMinutes: Long
        get() = if (startDateTime != null && endDateTime != null)
            Duration.between(startDateTime, endDateTime).toMinutes()
        else 60L

    companion object {
        fun empty(): Place = Place(
            id = "",
            placeId = "",
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

fun TripPlace.toUiModel(): Place = Place(
    id = this.tripPlaceId,
    placeId = this.placeId,
    name = this.title,
    latitude = this.latitude,
    longitude = this.longitude,
    address = this.address,
    startDateTime = if (this.startAt != null) LocalDateTime.parse(this.startAt) else null,
    endDateTime = if (this.endAt != null) LocalDateTime.parse(this.endAt) else null,
    categories = emptyList(),
    /** ImageItem 잘해봐야 함 */
    thumbnailImage = ImageItem(
        id = 0,
        url = this.thumbnail ?: "",
    ),
    images = emptyList()
)

fun PlaceListItem.toUiModel(): Place =
    Place(
        id = "",
        placeId = id,
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
            aspectRatio = thumbnailImageRatio
        ),
        images = emptyList()
    )

fun Place.toTimeBlock(dayStart: LocalDateTime): TimeBlock? {
    if (startDateTime == null || endDateTime == null) return null

    val dayOffset = ChronoUnit.DAYS.between(
        dayStart.toLocalDate(),
        startDateTime.toLocalDate()
    ).toInt()

    val dayIndex = dayOffset + 1

    if (dayIndex < 0) return null

    val startMinute =
        Duration.between(dayStart, startDateTime).toMinutes().toInt() - dayOffset * MINUTES_PER_DAY

    val durationMinute = durationMinutes.toInt()

    return TimeBlock(
        id = id,
        startMinute = startMinute,
        durationMinute = durationMinute,
        day = dayIndex,
        column = 0
    )
}
