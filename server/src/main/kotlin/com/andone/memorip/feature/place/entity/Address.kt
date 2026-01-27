package com.andone.memorip.feature.place.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Address protected constructor() {

    @Column(name = "region_1depth", nullable = false, length = 20)
    lateinit var region1Depth: String // 시/도 (ex. 서울)

    @Column(name = "region_2depth", length = 20)
    var region2Depth: String? = null // 구/군 (ex. 강남구)

    @Column(name = "region_3depth", length = 20)
    var region3Depth: String? = null // 동/읍/면 (ex. 역삼동)

    @Column(name = "full_address", nullable = false, length = 255)
    lateinit var fullAddress: String // 전체 주소

    companion object {
        fun create(
            region1Depth: String,
            region2Depth: String?,
            region3Depth: String?,
            fullAddress: String
        ): Address {
            require(region1Depth.isNotBlank()) { "시/도 정보는 필수입니다" }
            require(fullAddress.isNotBlank()) { "전체 주소는 필수입니다" }

            return Address().apply {
                this.region1Depth = region1Depth
                this.region2Depth = region2Depth
                this.region3Depth = region3Depth
                this.fullAddress = fullAddress
            }
        }
    }
}