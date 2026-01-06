package com.andone.memorip.domain.place.repository

import com.andone.memorip.domain.place.entity.Place
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceRepository : JpaRepository<Place, UUID>

