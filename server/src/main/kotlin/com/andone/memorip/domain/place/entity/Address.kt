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
//    init {
//        require(region1Depth.isNotBlank()) { "시/도 정보는 필수입니다" }
//        require(fullAddress.isNotBlank()) { "전체 주소는 필수입니다" }
//    }
//    protected constructor() : this("", null, null, "")
}