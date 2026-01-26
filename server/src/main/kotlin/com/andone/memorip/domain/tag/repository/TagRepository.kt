package com.andone.memorip.domain.tag.repository

import com.andone.memorip.domain.tag.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TagRepository : JpaRepository<Tag, UUID>