package com.andone.memorip.feature.group.repository

import com.andone.memorip.feature.group.entity.Group
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
                COALESCE(pi.related_place_images, '') as relatedPlaceImages,
                (CASE 
                WHEN :placeId IS NULL THEN false
                ELSE EXISTS (
                    SELECT 1 FROM places p 
                    WHERE p.group_id = g.id AND p.id = CAST(:placeId AS UUID) AND p.deleted_at IS NULL
                    UNION
                    SELECT 1 FROM group_places gp 
                    WHERE gp.group_id = g.id AND gp.place_id = CAST(:placeId AS UUID) AND gp.deleted_at IS NULL
                )
            END) as isPlaceAdded
            FROM groups g
            INNER JOIN users u ON g.owner_id = u.id
            LEFT JOIN LATERAL (
                SELECT COUNT(DISTINCT p.id) as place_count
                FROM (
                    SELECT p.id
                    FROM places p
                    WHERE p.group_id = g.id
                      AND p.deleted_at IS NULL
                    UNION
                    SELECT p.id
                    FROM group_places gp
                    INNER JOIN places p ON gp.place_id = p.id
                    WHERE gp.group_id = g.id
                      AND gp.deleted_at IS NULL
                      AND p.deleted_at IS NULL
                ) p
            ) pc ON true
            LEFT JOIN LATERAL (
                SELECT STRING_AGG(p.thumbnail_url, ',' ORDER BY p.created_at ASC) as related_place_images
                FROM (
                    SELECT DISTINCT p.thumbnail_url, p.created_at
                    FROM (
                        SELECT p.thumbnail_url, p.created_at
                        FROM places p
                        WHERE p.group_id = g.id
                          AND p.deleted_at IS NULL
                          AND p.thumbnail_url IS NOT NULL
                        UNION
                        SELECT p.thumbnail_url, p.created_at
                        FROM group_places gp
                        INNER JOIN places p ON gp.place_id = p.id
                        WHERE gp.group_id = g.id
                          AND gp.deleted_at IS NULL
                          AND p.deleted_at IS NULL
                          AND p.thumbnail_url IS NOT NULL
                    ) p
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
        @Param("placeId") placeId: UUID?,
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

    @Query("""
    SELECT 
        g.id as id,
        g.title as title,
        g.start_date as startDate,
        g.end_date as endDate
    FROM groups g
    WHERE (:ownerId IS NULL OR g.owner_id = :ownerId)
      AND(g.deleted_at IS NULL)
    """, nativeQuery = true)
    fun findSimpleGroups(@Param("ownerId") ownerId: UUID?): List<GroupPeriodProjection>
}