package com.andone.memorip.feature.place.repository

import com.andone.memorip.feature.place.entity.PlaceTag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceTagRepository : JpaRepository<PlaceTag, UUID> {
    fun findAllByPlaceId(id: UUID): List<PlaceTag>

    fun deleteByPlaceId(placeId: UUID): Int
}