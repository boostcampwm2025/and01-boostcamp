package com.andone.memorip.feature.user.repository

import com.andone.memorip.feature.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByFirebaseUid(firebaseUid: String): User?
}