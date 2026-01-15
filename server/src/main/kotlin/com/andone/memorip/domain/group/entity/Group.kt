package com.andone.memorip.domain.group.entity

import com.andone.memorip.common.entity.BaseTimeSyncEntity
import com.andone.memorip.common.util.UuidV7Generator
import com.andone.memorip.domain.user.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

enum class Visibility {
    PRIVATE,
    PUBLIC
}

enum class GroupType {
    DEFAULT,
    CUSTOM
}

@Entity
@Table(name = "groups")
@SQLDelete(sql = "UPDATE groups SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class Group protected constructor(
    id: UUID,
    owner: User,
    title: String,
    visibility: Visibility = Visibility.PRIVATE,
    type: GroupType = GroupType.CUSTOM
) : BaseTimeSyncEntity() {

    init {
        this.id = id
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: User = owner
        internal set

    @Column(nullable = false, length = 50)
    var title: String = title
        internal set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var visibility: Visibility = visibility
        internal set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var type: GroupType = type
        internal set

    fun updateTitle(title: String) {
        require(title.isNotBlank()) { "제목은 필수입니다" }
        require(title.length <= 50) { "제목은 50자 이하여야 합니다" }
        this.title = title
    }

    fun updateVisibility(visibility: Visibility) {
        this.visibility = visibility
    }
    fun isOwnedBy(userId: UUID): Boolean {
        return owner.id == userId
    }

    companion object {
        fun create(
            id: UUID? = null,
            owner: User,
            title: String,
            visibility: Visibility = Visibility.PRIVATE,
            type: GroupType = GroupType.CUSTOM
        ): Group {
            require(title.isNotBlank()) { "제목은 필수입니다" }
            require(title.length <= 50) { "제목은 50자 이하여야 합니다" }

            val generatedId = id ?: UuidV7Generator.generate()
            return Group(
                id = generatedId,
                owner = owner,
                title = title,
                visibility = visibility,
                type = type
            )
        }
    }
}