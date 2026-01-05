package com.andone.memorip.common.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseTimeSyncEntity : BaseTimeEntity() {
    
    @Column(name = "synced_at", columnDefinition = "TIMESTAMPTZ")
    var syncedAt: LocalDateTime? = null
        protected set
    
    fun sync() {
        syncedAt = LocalDateTime.now()
    }
}