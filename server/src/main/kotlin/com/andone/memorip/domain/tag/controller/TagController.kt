package com.andone.memorip.domain.tag.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.place.dto.response.PlaceListItemResponse
import com.andone.memorip.domain.tag.dto.request.TagCreateRequest
import com.andone.memorip.domain.tag.dto.response.TagResponse
import com.andone.memorip.domain.tag.dto.response.toTagCreateResponse
import com.andone.memorip.domain.tag.service.TagService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagService: TagService
) {

    @PostMapping
    fun createTag(
        @RequestBody req: TagCreateRequest
    ): ApiResult<TagResponse>{
        val result = tagService.createTag(req)
        return ApiResult.success(result.toTagCreateResponse())
    }

    @GetMapping
    fun getTagList(
        @PageableDefault(
            page = 0,
            size = 20,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        )
        pageable: Pageable
    ): ApiResult<List<TagResponse>> {
        val result = tagService.getTagList(pageable)
        return ApiResult.success(result.content, result.pagination)
    }
}
