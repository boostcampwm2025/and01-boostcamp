package com.andone.memorip.feature.user.service

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.feature.group.entity.Group
import com.andone.memorip.feature.group.entity.GroupType
import com.andone.memorip.feature.group.entity.Visibility
import com.andone.memorip.feature.group.repository.GroupRepository
import com.andone.memorip.feature.user.entity.User
import com.andone.memorip.feature.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {

    @Transactional
    fun getOrCreateMe(firebaseUid: String, nickname: String?): User {
        val existingUser = userRepository.findByFirebaseUid(firebaseUid)
        if (existingUser != null) {
            return existingUser
        }
        
        val newUser = userRepository.save(
            User.create(
                firebaseUid = firebaseUid,
                nickname = nickname ?: "User",
                profileImage = null
            )
        )
        
        groupRepository.save(
            Group.create(
                owner = newUser,
                title = "기본 그룹",
                visibility = Visibility.PRIVATE,
                type = GroupType.DEFAULT
            )
        )
        
        return newUser
    }

    @Transactional(readOnly = true)
    fun getById(userId: UUID): User {
        return userRepository.findByIdOrNull(userId)
            ?: throw BusinessException(code = CommonExceptionCode.USER_NOT_FOUND)
    }

    @Transactional
    fun updateNickname(userId: UUID, nickname: String): User {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw BusinessException(code = CommonExceptionCode.USER_NOT_FOUND)

        user.changeNickname(nickname)
        return user
    }
}