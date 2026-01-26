package com.andone.memorip.domain.tag.service

import com.andone.memorip.domain.tag.dto.request.TagCreateRequest
import com.andone.memorip.domain.tag.entity.Tag
import com.andone.memorip.domain.tag.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository
) {
    @Transactional
    fun createTag(req: TagCreateRequest): Tag {
        require(!tagRepository.existsByName(req.name)) {
            "이미 존재하는 태그명입니다"
        }

        val tag = Tag.create(
            name = req.name,
            colorHex = req.colorHex
        )

        return tagRepository.save(tag)
    }
}