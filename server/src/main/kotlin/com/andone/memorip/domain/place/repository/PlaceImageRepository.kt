package com.andone.memorip.domain.place.repository

import com.andone.memorip.domain.place.entity.PlaceImage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceImageRepository: JpaRepository<PlaceImage, UUID> {
}