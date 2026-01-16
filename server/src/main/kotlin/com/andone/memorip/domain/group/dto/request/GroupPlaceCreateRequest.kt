package com.andone.memorip.domain.group.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class GroupPlaceCreateRequest(
    @field:NotNull
    var placeId: UUID
)