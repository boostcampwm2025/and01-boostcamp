package com.andone.memorip.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val fullAddress: String,
    val region1Depth: String,
    val region2Depth: String? = null,
    val region3Depth: String? = null,
){
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