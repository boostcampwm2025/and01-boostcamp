package com.andone.memorip.domain.user.entity

import com.andone.memorip.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class User private constructor(
    @Id
    @Column(name = "id", columnDefinition = "UUID", nullable = false, updatable = false)
    val id: UUID,
    nickname: String,
    profileImage: String?
) : BaseEntity() {
    @Column(nullable = false, length = 30)
    var nickname: String = nickname
        private set

    @Column(name = "profile_image", nullable = true, length = 512)
    var profileImage: String? = null
        private set

    companion object {
        fun create(
            id: UUID = UUID.randomUUID(),
            nickname: String,
            profileImage: String? = null
        ): User {
            return User(
                id = id,
                nickname = nickname,
                profileImage = profileImage
            )
        }
    }
}