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
    var profileImage: String? = null
        internal set

    companion object {
        fun create(
            id: UUID? = null,
            nickname: String,
            profileImage: String? = null
        ): User {
            val generatedId = id ?: UuidV7Generator.generate()
            return User(
                id = generatedId,
                nickname = nickname,
                profileImage = profileImage
            )
        }
    }
}