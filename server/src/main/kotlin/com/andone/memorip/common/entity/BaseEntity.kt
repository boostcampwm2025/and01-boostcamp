package com.andone.memorip.common.entity

import jakarta.persistence.*
import java.util.UUID

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @Column(columnDefinition = "UUID")
    open lateinit var id: UUID
        protected set
}