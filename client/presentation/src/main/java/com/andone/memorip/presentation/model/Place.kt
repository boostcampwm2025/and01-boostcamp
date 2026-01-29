package com.andone.memorip.presentation.model

import android.util.Log
import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.GroupPlace
import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Immutable
data class Place(
    override val id: String,
    val placeId: String,
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

fun GroupPlace.toUiModel(): Place = Place(
    id = this.groupPlaceId,
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
        width = 0,
        height = 0
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
            width = 1,
            height = 1
        ),
        images = emptyList()
    )

fun Place.toTimeBlock(dayStart: LocalDateTime): TimeBlock? {
    if (startDateTime == null || endDateTime == null) return null
    Log.d("DEBUG TEST", "to time block call $this")

    val dayOffset = ChronoUnit.DAYS.between(
        dayStart.toLocalDate(),
        startDateTime.toLocalDate()
    ).toInt()
    Log.d("DEBUG TEST", "day offset : $dayOffset")

    val dayIndex = dayOffset + 1

    if (dayIndex < 0) return null

    val startMinute =
        Duration.between(dayStart, startDateTime).toMinutes().toInt() - dayOffset * MINUTES_PER_DAY
    Log.d("DEBUG TEST", "start minutes: $startMinute")

    val durationMinute = durationMinutes.toInt()

    Log.d("DEBUG TEST", "time block init ${durationMinutes}")

    return TimeBlock(
        id = id,
        startMinute = startMinute,
        durationMinute = durationMinute,
        day = dayIndex,
        column = 0
    )
}
