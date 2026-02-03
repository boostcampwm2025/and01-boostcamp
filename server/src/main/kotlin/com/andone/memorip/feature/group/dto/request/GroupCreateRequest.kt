package com.andone.memorip.feature.group.dto.request

import com.andone.memorip.feature.group.entity.Visibility
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupCreateRequest(
    @field:NotBlank(message = "그룹 제목은 필수입니다")
    @field:Size(max = 20, message = "그룹 제목은 20자 이하여야 합니다")
    @Schema(description = "그룹 제목", example = "제주도 여행", required = true)
    val title: String,
    
    @field:NotNull(message = "공개 여부는 필수입니다")
    @Schema(
        description = "그룹 공개 여부",
        example = "PUBLIC",
        allowableValues = ["PRIVATE", "PUBLIC"],
        required = true,
        defaultValue = "PUBLIC"
    )
    var visibility: Visibility = Visibility.PUBLIC
)