package com.andone.memorip.domain.group.dto.request

import com.andone.memorip.domain.group.entity.Visibility
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupCreateRequest(
    @field:NotBlank(message = "그룹 제목은 필수입니다")
    @field:Size(max = 50, message = "그룹 제목은 50자 이하여야 합니다")
    val title: String,
    
    @field:NotNull(message = "공개 여부는 필수입니다")
    var visibility: Visibility = Visibility.PRIVATE
)