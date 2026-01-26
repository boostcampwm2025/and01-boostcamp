package com.andone.memorip.feature.place.repository

import com.andone.memorip.feature.place.entity.GroupPlace
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface GroupPlaceRepository : JpaRepository<GroupPlace, UUID> {
    @Query("SELECT gp FROM GroupPlace gp WHERE gp.place.id = :placeId ORDER BY gp.createdAt ASC")
    fun findByPlaceId(@Param("placeId") placeId: UUID): List<GroupPlace>
    
    @Query("SELECT gp FROM GroupPlace gp WHERE gp.group.id = :groupId ORDER BY gp.createdAt ASC")
    fun findByGroupId(@Param("groupId") groupId: UUID): List<GroupPlace>

    fun findAllByGroupId(groupId: UUID): List<GroupPlace>
}