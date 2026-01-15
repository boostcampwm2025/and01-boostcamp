package com.andone.memorip.domain.user.entity

import com.andone.memorip.common.entity.BaseTimeSyncEntity
import com.andone.memorip.common.util.UuidV7Generator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class User protected constructor(
    id: UUID,
    @Column(name = "firebase_uid", nullable = false, unique = true, length = 128)
    val firebaseUid: String,
    nickname: String,
    profileImage: String?
) : BaseTimeSyncEntity() {
    
    init {
        this.id = id
    }

    @Column(nullable = false, length = 30)
    var nickname: String = nickname
        internal set

    @Column(name = "profile_image", nullable = true, length = 512)
    var profileImage: String? = profileImage
        internal set

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    companion object {
        private const val DEFAULT_PROFILE_IMAGE = "https://api.dicebear.com/7.x/identicon/svg?seed=memorip"

        fun create(
            id: UUID? = null,
            firebaseUid: String,
            nickname: String,
            profileImage: String? = null
        ): User {
            val generatedId = id ?: UuidV7Generator.generate()
            return User(
                id = generatedId,
                firebaseUid = firebaseUid,
                nickname = nickname,
                profileImage = profileImage ?: DEFAULT_PROFILE_IMAGE
            )
        }
    }
}