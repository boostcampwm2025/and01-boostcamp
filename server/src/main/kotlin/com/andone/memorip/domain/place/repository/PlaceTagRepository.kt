package com.andone.memorip.domain.place.repository

import com.andone.memorip.domain.place.entity.PlaceTag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceTagRepository : JpaRepository<PlaceTag, UUID> {
}