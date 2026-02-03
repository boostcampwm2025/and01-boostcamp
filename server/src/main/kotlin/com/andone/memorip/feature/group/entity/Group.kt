package com.andone.memorip.feature.group.entity

import com.andone.memorip.common.entity.BaseTimeSyncEntity
import com.andone.memorip.common.util.UuidV7Generator
import com.andone.memorip.feature.user.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate
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

    @Column(nullable = false, length = 20)
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

    @Column(name = "start_date")
    var startDate: LocalDate? = null
        internal set

    @Column(name = "end_date")
    var endDate: LocalDate? = null
        internal set

    fun updatePeriod(start: LocalDate?, end: LocalDate?) {
        if (start != null && end != null) {
            require(!end.isBefore(start)) { "종료일은 시작일보다 빠를 수 없습니다" }

            val maxEnd = start.plusMonths(1)
            require(!end.isAfter(maxEnd)) {
                "기간은 시작일 기준 최대 1개월까지만 가능합니다"
            }
        }

        this.startDate = start
        this.endDate = end
    }

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
            type: GroupType = GroupType.CUSTOM,
            startDate: LocalDate? = null,
            endDate: LocalDate? = null
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
            ).apply {
                updatePeriod(startDate, endDate)
            }
        }
    }
}