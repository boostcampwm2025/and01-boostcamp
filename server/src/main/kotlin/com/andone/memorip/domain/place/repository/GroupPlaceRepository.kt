package com.andone.memorip.domain.place.repository

import com.andone.memorip.domain.place.entity.GroupPlace
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GroupPlaceRepository : JpaRepository<GroupPlace, UUID>