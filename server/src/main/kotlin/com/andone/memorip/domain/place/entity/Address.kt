package com.andone.memorip.domain.place.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Address(
    @Column(name = "region_1depth", nullable = false, length = 20)
    var region1Depth: String, // 시/도 (ex. 서울)

    @Column(name = "region_2depth", length = 20)
    var region2Depth: String? = null, // 구/군 (ex. 강남구)

    @Column(name = "region_3depth", length = 20)
    var region3Depth: String? = null, // 동/읍/면 (ex. 역삼동)

    @Column(name = "full_address", nullable = false, length = 255)
    var fullAddress: String // 전체 주소
) {
    companion object {
        fun from(fullAddress: String): Address {
            val parts = fullAddress.trim().split("\\s+".toRegex())

            val depth1 = parts.getOrElse(0) { "" }
            val depth2 = parts.getOrNull(1)
            val depth3 = parts.getOrNull(2)

            return Address(
                region1Depth = depth1,
                region2Depth = depth2,
                region3Depth = depth3,
                fullAddress = fullAddress
            )
        }
    }
}