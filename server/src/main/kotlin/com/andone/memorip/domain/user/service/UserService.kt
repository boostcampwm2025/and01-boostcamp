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

    @Transactional
    fun updateNickname(firebaseUid: String, nickname: String): User {
        val user = userRepository.findByFirebaseUid(firebaseUid)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        user.changeNickname(nickname)
        return user
    }
}