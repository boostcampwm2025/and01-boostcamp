package com.andone.memorip.feature.tag.repository

import com.andone.memorip.feature.tag.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TagRepository : JpaRepository<Tag, UUID>{
    fun existsByName(name: String): Boolean
}