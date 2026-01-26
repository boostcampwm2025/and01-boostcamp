package com.andone.memorip.domain.place.repository

import com.andone.memorip.domain.place.entity.GroupPlace
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface GroupPlaceRepository : JpaRepository<GroupPlace, UUID> {
    @Query("SELECT gp.group.id FROM GroupPlace gp WHERE gp.place.id = :placeId AND gp.deletedAt IS NULL")
    fun findGroupIdsByPlaceId(@Param("placeId") placeId: UUID): List<UUID>
    
    @Query("SELECT gp FROM GroupPlace gp WHERE gp.group.id IN :groupIds AND gp.place.id = :placeId AND gp.deletedAt IS NULL")
    fun findByGroupIdsAndPlaceId(
        @Param("groupIds") groupIds: List<UUID>,
        @Param("placeId") placeId: UUID
    ): List<GroupPlace>
    
    @Modifying
    @Query("DELETE FROM GroupPlace gp WHERE gp.group.id IN :groupIds AND gp.place.id = :placeId")
    fun deleteByGroupIdsAndPlaceId(
        @Param("groupIds") groupIds: List<UUID>,
        @Param("placeId") placeId: UUID
    ): Int
}