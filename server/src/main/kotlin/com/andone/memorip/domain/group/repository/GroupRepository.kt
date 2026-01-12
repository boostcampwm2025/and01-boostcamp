package com.andone.memorip.domain.group.repository

import com.andone.memorip.domain.group.entity.Group
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GroupRepository: JpaRepository<Group, UUID> {
}