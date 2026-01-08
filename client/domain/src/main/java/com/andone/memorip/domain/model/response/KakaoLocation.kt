package com.andone.memorip.domain.model.response

data class KakaoLocation(
    val id: String,
    val title: String,
    val category: String,
    val address: String,
    val roadAddress: String,
    val longitude: String,
    val latitude: String
)