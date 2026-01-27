package com.andone.memorip.feature.place.repository

import com.andone.memorip.feature.place.entity.Place
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface PlaceRepository : JpaRepository<Place, UUID> {

    fun findAllByIsPublicTrue(pageable: Pageable): Page<Place>

    @Query(
        value = """
        SELECT p FROM Place p
        INNER JOIN GroupPlace gp ON gp.place.id = p.id
        WHERE gp.group.id = :groupId
        ORDER BY p.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(p) FROM Place p
        INNER JOIN GroupPlace gp ON gp.place.id = p.id
        WHERE gp.group.id = :groupId
    """
    )
    fun findAllByGroupId(@Param("groupId") groupId: UUID, pageable: Pageable): Page<Place>

    @Query("""
    SELECT DISTINCT p FROM Place p
    LEFT JOIN p.placeTags pt
    WHERE (:query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')))
      AND (:region1Depth IS NULL OR p.address.region1Depth = :region1Depth)
      AND (:region2Depth IS NULL OR p.address.region2Depth = :region2Depth)
      AND (:tagIds IS NULL OR pt.tag.id IN :tagIds)
    """)
    fun searchPlaces(
        @Param("query") query: String?,
        @Param("tagIds") tagIds: List<UUID>?,
        @Param("region1Depth") region1Depth: String?,
        @Param("region2Depth") region2Depth: String?,
        pageable: Pageable
    ): Page<Place>
}