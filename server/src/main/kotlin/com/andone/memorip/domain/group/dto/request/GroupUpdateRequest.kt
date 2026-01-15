package com.andone.memorip.domain.group.dto.request

import com.andone.memorip.domain.group.entity.Visibility
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupUpdateRequest(
    @field:NotBlank(message = "그룹 제목은 필수입니다")
    @field:Size(max = 50, message = "그룹 제목은 50자 이하여야 합니다")
    @Schema(description = "그룹 제목", example = "제주도 여행", required = true)
    val title: String,

    @field:NotNull(message = "공개 여부는 필수입니다")
    @Schema(
        description = "그룹 공개 여부",
        example = "PRIVATE",
        allowableValues = ["PRIVATE", "PUBLIC"],
        required = true
    )
    var visibility: Visibility
)