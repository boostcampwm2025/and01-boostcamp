package com.andone.memorip.data.kakaosearch.model

import com.andone.memorip.domain.model.response.KakaoLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoSearchLocation(
    @SerialName("documents") val locations: List<KakaoLocationResponse>,
    @SerialName("meta") val meta: KakaoMeta
)

@Serializable
data class KakaoMeta(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("pageable_count") val pageableCount: Int,
    @SerialName("is_end") val isEnd: Boolean
)

@Serializable
data class KakaoLocationResponse(
    @SerialName("id") val id: String,
    @SerialName("place_name") val title: String,
    @SerialName("category_name") val category: String,
    @SerialName("address_name") val address: String,
    @SerialName("road_address_name") val roadAddress: String,
    @SerialName("x") val longitude: String,
    @SerialName("y") val latitude: String
)

fun KakaoLocationResponse.toDomain(): KakaoLocation =
    KakaoLocation(
        id = id,
        title = title,
        category = category,
        address = address,
        roadAddress = roadAddress,
        longitude = longitude,
        latitude = latitude
    )

