package com.andone.memorip.common.entity

import com.andone.memorip.common.util.UuidV7Generator
import jakarta.persistence.*
import java.util.UUID

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @Column(columnDefinition = "UUID")
    open var id: UUID? = null
        protected set

    @PrePersist
    fun generateId() {
        if (id == null) {
            id = UuidV7Generator.generate()
        }
    }
}