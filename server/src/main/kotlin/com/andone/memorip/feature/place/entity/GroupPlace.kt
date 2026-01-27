package com.andone.memorip.feature.place.entity

import com.andone.memorip.common.entity.BaseTimeSyncEntity
import com.andone.memorip.common.util.UuidV7Generator
import com.andone.memorip.feature.group.entity.Group
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "group_places")
class GroupPlace protected constructor(
    id: UUID,
    group: Group,
    place: Place
) : BaseTimeSyncEntity() {

    init {
        this.id = id
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    var group: Group = group
        internal set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    var place: Place = place
        internal set

    @Column(name = "start_at", columnDefinition = "TIMESTAMPTZ")
    var startAt: LocalDateTime? = null
        internal set

    @Column(name = "end_at", columnDefinition = "TIMESTAMPTZ")
    var endAt: LocalDateTime? = null
        internal set

    companion object {
        fun create(
            id: UUID? = null,
            group: Group,
            place: Place
        ): GroupPlace {
            val generatedId = id ?: UuidV7Generator.generate()
            return GroupPlace(
                id = generatedId,
                group = group,
                place = place
            )
        }
    }
}