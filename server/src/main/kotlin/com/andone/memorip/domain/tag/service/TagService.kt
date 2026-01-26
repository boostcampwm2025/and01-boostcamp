package com.andone.memorip.domain.tag.service

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.PlaceListResult
import com.andone.memorip.domain.place.dto.response.PlaceListItemResponse
import com.andone.memorip.domain.tag.dto.TagListResult
import com.andone.memorip.domain.tag.dto.request.TagCreateRequest
import com.andone.memorip.domain.tag.dto.response.TagResponse
import com.andone.memorip.domain.tag.entity.Tag
import com.andone.memorip.domain.tag.repository.TagRepository
import org.springframework.data.domain.Pageable
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

    fun getTagList(pageable: Pageable): TagListResult {
        val page = tagRepository.findAll(pageable)

        val content = page.content.map { tag ->
            TagResponse(
                id = tag.id,
                name = tag.name,
                colorHex = tag.colorHex,
            )
        }

        val pagination = ApiResult.PaginationInfo(
            currentPage = page.number + 1,
            totalPages = page.totalPages,
            totalCount = page.totalElements,
            hasNext = page.hasNext()
        )

        return TagListResult(content, pagination)
    }
}