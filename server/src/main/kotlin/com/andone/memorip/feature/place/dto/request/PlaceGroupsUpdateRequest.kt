package com.andone.memorip.feature.place.dto.request

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.AssertTrue
import java.util.UUID

data class PlaceGroupsUpdateRequest(
    val addGroupIds: List<UUID> = emptyList(),
    val removeGroupIds: List<UUID> = emptyList()
) {
    @get:AssertTrue(message = "addGroupIds와 removeGroupIds 중 최소 하나는 비어있지 않아야 합니다")
    @get:JsonIgnore
    val isValid: Boolean
        get() = addGroupIds.isNotEmpty() || removeGroupIds.isNotEmpty()
}
