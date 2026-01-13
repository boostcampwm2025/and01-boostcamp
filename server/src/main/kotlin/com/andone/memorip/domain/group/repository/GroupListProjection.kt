package com.andone.memorip.domain.group.repository

import com.andone.memorip.domain.group.entity.GroupType
import com.andone.memorip.domain.group.entity.Visibility
import java.time.LocalDateTime
import java.util.UUID

/**
 * Group 리스트 조회용 Projection
 * 그룹 데이터만 포함 (페이징 정보는 별도 쿼리로 조회)
 */
data class GroupListProjection(
    val id: UUID,
    val ownerId: UUID,
    val ownerNickname: String,
    val ownerProfileImage: String?,
    val title: String,
    val visibility: Visibility,
    val type: GroupType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val placeCount: Long,
    val relatedPlaceImages: String? // 쉼표로 구분된 이미지 URL 문자열
)