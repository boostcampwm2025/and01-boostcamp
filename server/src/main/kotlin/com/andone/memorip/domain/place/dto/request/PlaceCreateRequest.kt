package com.andone.memorip.domain.place.dto.request

import com.andone.memorip.domain.place.entity.Address
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.util.*

data class PlaceCreateRequest(

    @field:NotNull
    val writerId: UUID,

    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    val content: String? = null,

    val tag: List<String>? = null,

    @field:NotNull
    val groupId: UUID,

    @field:NotNull
    val latitude: Double,

    @field:NotNull
    val longitude: Double,

    @field:NotBlank(message = "주소는 필수입니다.")
    val address: Address,

    @field:NotBlank(message = "이미지는 필수입니다.")
    val imageUrls: List<String>,

    @field:NotBlank(message = "공개 여부는 필수입니다.")
    val isPublic: Boolean
)