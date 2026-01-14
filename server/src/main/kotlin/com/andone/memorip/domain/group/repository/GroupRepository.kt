package com.andone.memorip.domain.group.repository

import com.andone.memorip.domain.group.entity.Group
import com.andone.memorip.domain.group.entity.Visibility
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GroupRepository: JpaRepository<Group, UUID> {
    fun findAllByVisibility(visibility: Visibility, pageable: Pageable): Page<Group>
    fun findAllByOwnerId(ownerId: UUID, pageable: Pageable): Page<Group>
}