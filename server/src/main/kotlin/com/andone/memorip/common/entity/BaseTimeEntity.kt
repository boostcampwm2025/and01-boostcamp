package com.andone.memorip.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity : BaseEntity() {
    
    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set
    
    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMPTZ")
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set
    
    @Column(name = "deleted_at", columnDefinition = "TIMESTAMPTZ")
    var deletedAt: LocalDateTime? = null
        protected set
    
    fun isDeleted(): Boolean = deletedAt != null
}