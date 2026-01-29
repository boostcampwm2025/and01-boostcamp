package com.andone.memorip.common.security

import java.util.UUID

data class UserPrincipal(
    val userId: UUID,
    val firebaseUid: String
)