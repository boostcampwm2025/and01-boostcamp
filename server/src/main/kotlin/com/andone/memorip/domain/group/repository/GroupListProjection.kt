package com.andone.memorip.domain.group.repository

import com.andone.memorip.domain.group.entity.GroupType
import com.andone.memorip.domain.group.entity.Visibility
import java.time.LocalDateTime
import java.util.UUID

interface GroupListProjection {
    fun getId(): UUID
    fun getOwnerId(): UUID
    fun getOwnerNickname(): String
    fun getOwnerProfileImage(): String?
    fun getTitle(): String
    fun getVisibility(): Visibility
    fun getType(): GroupType
    fun getCreatedAt(): LocalDateTime
    fun getUpdatedAt(): LocalDateTime
    fun getPlaceCount(): Long
    fun getRelatedPlaceImages(): String?
}