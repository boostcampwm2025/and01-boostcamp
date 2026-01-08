package com.andone.memorip.domain.model.request

import com.andone.memorip.domain.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlaceCreateRequest(
    @Serializable(with = UUIDSerializer::class)
    val writerId: UUID,
    val title: String,
    val content: String? = null,
    val tag: List<String> = emptyList(),
    @Serializable(with = UUIDSerializer::class)
    val groupId: UUID,
    val latitude: Double,
    val longitude: Double,
    val address: Address,
    val imageUrls: List<String>
)