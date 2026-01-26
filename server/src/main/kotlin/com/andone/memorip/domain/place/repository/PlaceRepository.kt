package com.andone.memorip.domain.place.repository

import com.andone.memorip.domain.place.entity.Place
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
}