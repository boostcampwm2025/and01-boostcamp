package com.andone.memorip.domain.tag.entity

import com.andone.memorip.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "tags")
class Tag protected constructor(
    id: UUID? = null,
    name: String,
    colorHex: String
) : BaseEntity() {
    
    init {
        this.id = id
    }
    
    @Column(nullable = false, length = 20)
    var name: String = name
        internal set
    
    @Column(name = "color_hex", nullable = false, length = 9)
    var colorHex: String = colorHex
        internal set
    
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
            return Tag(id, name, colorHex)
        }
    }
}