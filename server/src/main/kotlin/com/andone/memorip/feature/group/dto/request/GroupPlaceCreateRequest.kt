package com.andone.memorip.feature.group.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class GroupPlaceCreateRequest(
    @field:NotNull
    var placeId: UUID
)