package com.andone.memorip.domain.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoSearchResponse(
    @SerialName("documents") val locations: List<KakaoLocation>,
    @SerialName("meta") val meta: KakaoMeta
)

@Serializable
data class KakaoMeta(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("pageable_count") val pageableCount: Int,
    @SerialName("is_end") val isEnd: Boolean
)

@Serializable
data class KakaoLocation(
    @SerialName("id") val id: String,
    @SerialName("place_name") val title: String,
    @SerialName("category_name") val category: String,
    @SerialName("address_name") val address: String,
    @SerialName("road_address_name") val roadAddress: String,
    @SerialName("x") val longitude: String,
    @SerialName("y") val latitude: String
)