package com.andone.memorip.domain.tag.entity

import com.andone.memorip.common.entity.BaseTimeSyncEntity
import com.andone.memorip.common.util.UuidV7Generator
import com.andone.memorip.domain.place.entity.PlaceTag
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.UUID

@Entity
@Table(name = "tags")
@SQLDelete(sql = "UPDATE tags SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class Tag protected constructor(
    id: UUID? = null,
    name: String,
    colorHex: String
) : BaseTimeSyncEntity() {
    
    init {
        this.id = id
    }
    
    @Column(nullable = false, length = 20)
    var name: String = name
        internal set
    
    @Column(name = "color_hex", nullable = false, length = 9)
    var colorHex: String = colorHex
        internal set
    
    // 양방향 연관관계 설정: Tag가 사용된 PlaceTag 컬렉션
    @OneToMany(
        mappedBy = "tag",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    private val placeTags: MutableList<PlaceTag> = mutableListOf()

    fun getPlaceTags(): List<PlaceTag> = placeTags.toList()
    
    fun updateName(name: String) {
        require(name.isNotBlank()) { "태그명은 필수입니다" }
        require(name.length <= 20) { "태그명은 20자 이하여야 합니다" }
        this.name = name
    }
    
    fun updateColor(colorHex: String) {
        require(colorHex.matches(Regex("^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?\$"))) {
            "올바른 HEX 색상 코드가 아닙니다"
        }
        this.colorHex = colorHex
    }
    
    companion object {
        fun create(
            id: UUID? = null,
            name: String,
            colorHex: String
        ): Tag {
            require(name.isNotBlank()) { "태그명은 필수입니다" }
            require(name.length <= 20) { "태그명은 20자 이하여야 합니다" }
            require(colorHex.matches(Regex("^#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?\$"))) {
                "올바른 HEX 색상 코드가 아닙니다"
            }
            
            val generatedId = id ?: UuidV7Generator.generate()
            return Tag(generatedId, name, colorHex)
        }
    }
}