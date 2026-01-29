package com.andone.memorip.feature.place.dto.request

import com.andone.memorip.feature.place.entity.Address
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class PlaceRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "장소 제목", example = "카페 404", required = true)
    val title: String,

    @Schema(description = "장소 설명", example = "67 커피", nullable = true)
    val content: String? = null,

    @Schema(description = "태그 ID 목록", example = "[\"4ac0f4ca-26e7-4813-8bb3-07fca0a40469\"]", nullable = true)
    val tags: List<UUID>? = null,

    @field:NotEmpty(message = "그룹은 최소 1개 이상 선택해야 합니다.")
    @Schema(description = "장소가 속할 그룹 ID 목록", example = "[\"019bf7f5-14c4-71c5-8e8c-f6fd477eb59c\"]", required = true)
    val groupIds: List<UUID>,

    @field:NotNull(message = "위도는 필수입니다.")
    @Schema(description = "위도", example = "37.5445", required = true)
    var latitude: Double,

    @field:NotNull(message = "경도는 필수입니다.")
    @Schema(description = "경도", example = "127.0550", required = true)
    var longitude: Double,

    @field:NotNull(message = "주소는 필수입니다.")
    @Schema(description = "주소 정보", required = true)
    var address: Address,

    @field:NotEmpty(message = "이미지는 최소 1개 이상 필요합니다.")
    @Schema(
        description = "이미지 URL 목록",
        example = "[\"https://kr.object.ncloudstorage.com/memorip/place/b10ebcad-59c3-40ff-ab40-ba2db6897c2f.jpg\", \"https://kr.object.ncloudstorage.com/memorip/place/421131bd-55a0-4c85-b7a0-4d38d083eb1c.jpg\"]",
        required = true
    )
    var imageUrls: List<String>,

    @field:NotNull(message = "썸네일 이미지 비율은 필수입니다.")
    @Schema(description = "썸네일 이미지 비율 (width / height)", example = "1.5", required = true)
    var thumbnailImageRatio: Float,

    @get:JsonProperty("isPublic")
    @field:NotNull(message = "공개 여부는 필수입니다.")
    @Schema(description = "공개 여부", example = "true", required = true)
    var isPublic: Boolean
)
