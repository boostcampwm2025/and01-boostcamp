package com.andone.memorip.domain.user.service

import com.andone.memorip.domain.user.entity.User
import com.andone.memorip.domain.user.repository.UserRepository
import org.springframework.transaction.annotation.Transactional

class UserService(private val userRepository: UserRepository) {

    @Transactional
    fun getOrCreateMe(firebaseUid: String, nickname: String?): User {
        return userRepository.findByFirebaseUid(firebaseUid)
            ?: userRepository.save(
                User.create(
                    firebaseUid = firebaseUid,
                    nickname = nickname ?: "사용자",
                    profileImage = null
                )
            )
    }
}