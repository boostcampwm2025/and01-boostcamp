package com.andone.memorip.domain.place.entity

import com.andone.memorip.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "place_tags")
class PlaceTag protected constructor(
    id: UUID? = null,
    placeId: UUID,
    tagId: UUID
) : BaseEntity() {
    
    init {
        this.id = id
    }
    
    @Column(name = "place_id", nullable = false, columnDefinition = "UUID")
    var placeId: UUID = placeId
        internal set
    
    @Column(name = "tag_id", nullable = false, columnDefinition = "UUID")
    var tagId: UUID = tagId
        internal set
    
    companion object {
        fun create(
            id: UUID? = null,
            placeId: UUID,
            tagId: UUID
        ): PlaceTag {
            return PlaceTag(id, placeId, tagId)
        }
    }
}