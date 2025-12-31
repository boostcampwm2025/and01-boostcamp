package com.andone.memorip.domain.model.response

data class KakaoSearchResponse(
    val documents: List<KakaoLocation>,
    val meta: KakaoMeta
)

data class KakaoMeta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
)

data class KakaoLocation(
    val id: String,
    val place_name: String,
    val category_name: String,
    val address_name: String,
    val road_address_name: String,
    val x: String,
    val y: String
)