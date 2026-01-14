package com.andone.memorip.domain.place.entity

import com.andone.memorip.common.entity.BaseTimeEntity
import com.andone.memorip.common.util.UuidV7Generator
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "place_images")
@SQLDelete(sql = "UPDATE place_images SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class PlaceImage protected constructor(
    id: UUID,
    place: Place,
    url: String
) : BaseTimeEntity() {
    
    init {
        this.id = id
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    var place: Place = place
        internal set
    
    @Column(nullable = false, length = 512)
    var url: String = url
        internal set
    
    companion object {
        fun create(
            id: UUID? = null,
            place: Place,
            url: String
        ): PlaceImage {
            require(url.isNotBlank()) { "이미지 URL은 필수입니다" }
            require(url.length <= 512) { "이미지 URL은 512자 이하여야 합니다" }
            
            val generatedId = id ?: UuidV7Generator.generate()
            return PlaceImage(generatedId, place, url)
        }
    }
}