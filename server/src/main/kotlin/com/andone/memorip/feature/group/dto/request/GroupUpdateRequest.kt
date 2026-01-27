package com.andone.memorip.feature.group.dto.request

import com.andone.memorip.feature.group.entity.Visibility
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

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
    var visibility: Visibility,

    @Schema(description = "시작 날짜", example = "2024-03-01", required = false)
    val startDate: LocalDate? = null,

    @Schema(description = "종료 날짜", example = "2024-03-31", required = false)
    val endDate: LocalDate? = null
)