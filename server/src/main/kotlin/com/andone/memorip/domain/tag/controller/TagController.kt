package com.andone.memorip.domain.tag.controller

import com.andone.memorip.common.response.ApiResult
import com.andone.memorip.domain.tag.dto.request.TagCreateRequest
import com.andone.memorip.domain.tag.dto.response.TagCreateResponse
import com.andone.memorip.domain.tag.dto.response.toTagCreateResponse
import com.andone.memorip.domain.tag.service.TagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagService: TagService
) {

    @PostMapping
    fun createTag(
        @RequestBody req: TagCreateRequest
    ): ApiResult<TagCreateResponse>{
        val result = tagService.createTag(req)
        return ApiResult.success(result.toTagCreateResponse())
    }
}
