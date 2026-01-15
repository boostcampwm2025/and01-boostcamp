package com.andone.memorip.domain.group.repository

import com.andone.memorip.domain.group.entity.Group
import com.andone.memorip.domain.group.entity.Visibility
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface GroupRepository: JpaRepository<Group, UUID> {
    @Query(
        value = """
            SELECT 
                g.id as id,
                u.id as ownerId,
                u.nickname as ownerNickname,
                u.profile_image as ownerProfileImage,
                g.title as title,
                g.visibility as visibility,
                g.type as type,
                g.created_at as createdAt,
                g.updated_at as updatedAt,
                COALESCE(pc.place_count, 0) as placeCount,
                COALESCE(pi.related_place_images, '') as relatedPlaceImages
            FROM groups g
            INNER JOIN users u ON g.owner_id = u.id
            LEFT JOIN LATERAL (
                SELECT COUNT(*) as place_count
                FROM places p
                WHERE p.group_id = g.id
                  AND p.deleted_at IS NULL
            ) pc ON true
            LEFT JOIN LATERAL (
                SELECT STRING_AGG(p.thumbnail_url, ',' ORDER BY p.created_at ASC) as related_place_images
                FROM (
                    SELECT p.thumbnail_url, p.created_at
                    FROM places p
                    WHERE p.group_id = g.id
                      AND p.deleted_at IS NULL
                      AND p.thumbnail_url IS NOT NULL
                    ORDER BY p.created_at ASC
                    LIMIT 7
                ) p
            ) pi ON true
            WHERE (:ownerId IS NULL OR g.owner_id = :ownerId)
              AND (:visibility IS NULL OR g.visibility = CAST(:visibility AS VARCHAR))
              AND g.deleted_at IS NULL
              AND u.deleted_at IS NULL
            ORDER BY g.created_at DESC
            LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true
    )
    fun findGroupListByOwnerId(
        @Param("ownerId") ownerId: UUID?,
        @Param("visibility") visibility: String?,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int
    ): List<GroupListProjection>

    @Query(
        value = """
            SELECT COUNT(*)
            FROM groups g
            WHERE (:ownerId IS NULL OR g.owner_id = :ownerId)
              AND (:visibility IS NULL OR g.visibility = CAST(:visibility AS VARCHAR))
              AND g.deleted_at IS NULL
        """,
        nativeQuery = true
    )
    fun countGroups(
        @Param("ownerId") ownerId: UUID?,
        @Param("visibility") visibility: String?
    ): Long
}