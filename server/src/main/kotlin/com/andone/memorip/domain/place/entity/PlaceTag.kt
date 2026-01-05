package com.andone.memorip.domain.place.entity

import com.andone.memorip.common.entity.BaseTimeSyncEntity
import com.andone.memorip.common.util.UuidV7Generator
import com.andone.memorip.domain.tag.entity.Tag
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "place_tags")
@SQLDelete(sql = "UPDATE place_tags SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class PlaceTag protected constructor(
    id: UUID? = null,
    place: Place,
    tag: Tag
) : BaseTimeSyncEntity() {
    
    init {
        this.id = id
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    var place: Place = place
        internal set
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    var tag: Tag = tag
        internal set
    
    companion object {
        fun create(
            id: UUID? = null,
            place: Place,
            tag: Tag
        ): PlaceTag {
            val generatedId = id ?: UuidV7Generator.generate()
            return PlaceTag(generatedId, place, tag)
        }
    }
}