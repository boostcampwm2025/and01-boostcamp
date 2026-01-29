package com.andone.memorip.feature.place.dto.request

import com.andone.memorip.feature.place.entity.Address
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class PlaceRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    val content: String? = null,

    val tag: List<String>? = null,

    @field:NotEmpty(message = "그룹은 최소 1개 이상 선택해야 합니다.")
    val groupIds: List<UUID>,

    @field:NotNull(message = "위도는 필수입니다.")
    var latitude: Double,

    @field:NotNull(message = "경도는 필수입니다.")
    var longitude: Double,

    @field:NotNull(message = "주소는 필수입니다.")
    var address: Address,

    @field:NotEmpty(message = "이미지는 최소 1개 이상 필요합니다.")
    var imageUrls: List<String>,

    @field:NotNull(message = "공개 여부는 필수입니다.")
    var isPublic: Boolean
)
