package com.andone.memorip.feature.place.repository

import com.andone.memorip.feature.place.entity.PlaceImage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceImageRepository: JpaRepository<PlaceImage, UUID> {
    fun findAllByPlaceId(id: UUID): List<PlaceImage>
}