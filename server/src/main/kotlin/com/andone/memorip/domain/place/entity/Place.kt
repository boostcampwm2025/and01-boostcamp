package com.andone.memorip.domain.place.entity

import com.andone.memorip.common.entity.BaseTimeSyncEntity
import com.andone.memorip.common.util.UuidV7Generator
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "places")
@SQLDelete(sql = "UPDATE places SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class Place protected constructor(
    id: UUID? = null,
    groupId: UUID,
    writerId: UUID,
    title: String,
    content: String? = null,
    latitude: Double,
    longitude: Double,
    address: Address,
    imageUrls: List<String>
) : BaseTimeSyncEntity() {

    init {
        this.id = id
    }
    
    @Column(name = "group_id", nullable = false, columnDefinition = "UUID")
    var groupId: UUID = groupId
        internal set

    @Column(name = "parent_place_id", columnDefinition = "UUID")
    var parentPlaceId: UUID? = null
        internal set
    
    @Column(name = "writer_id", nullable = false, columnDefinition = "UUID")
    var writerId: UUID = writerId
        internal set

    @Column(nullable = false, length = 30)
    var title: String = title
        internal set

    @Column(columnDefinition = "TEXT")
    var content: String? = content
        internal set

    @Column(nullable = false)
    var latitude: Double = latitude
        internal set

    @Column(nullable = false)
    var longitude: Double = longitude
        internal set

    @Embedded
    var address: Address = address
        internal set

    @Column(name = "start_at", columnDefinition = "TIMESTAMPTZ")
    var startAt: LocalDateTime? = null
        internal set

    @Column(name = "end_at", columnDefinition = "TIMESTAMPTZ")
    var endAt: LocalDateTime? = null
        internal set

    // 양방향 연관관계 설정: Place의 이미지 컬렉션
    @OneToMany(
        mappedBy = "place",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    private val images: MutableList<PlaceImage> = mutableListOf()

    init {
        imageUrls.forEach { url ->
            this.addImage(url)
        }
    }

    @OneToMany(
        mappedBy = "place",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    private val placeTags: MutableList<PlaceTag> = mutableListOf()

    fun getImages(): List<PlaceImage> = images.toList()

    fun getPlaceTags(): List<PlaceTag> = placeTags.toList()

    fun addImage(url: String, id: UUID? = null): PlaceImage {
        val newImage = PlaceImage.create(id = id, place = this, url = url)
        images.add(newImage)
        return newImage
    }

    fun updateImages(newUrls: List<String>) {
        // todo: object storage의 사진들 삭제, 추가 로직 넣어서 사용해야 함. -> service에서 할 듯?
        images.clear()
        newUrls.forEach { addImage(it) }
    }

    fun updateTitle(title: String) {
        require(title.isNotBlank()) { "제목은 필수입니다" }
        require(title.length <= 30) { "제목은 30자 이하여야 합니다" }
        this.title = title
    }

    fun updateContent(content: String?) {
        this.content = content
    }

    private fun updateLocation(latitude: Double, longitude: Double) {
        require(latitude in -90.0..90.0) { "위도는 -90 ~ 90 범위여야 합니다" }
        require(longitude in -180.0..180.0) { "경도는 -180 ~ 180 범위여야 합니다" }
        this.latitude = latitude
        this.longitude = longitude
    }

    private fun updateAddress(newAddress: Address) {
        this.address = newAddress
    }

    fun updateLocationAndAddress(
        latitude: Double,
        longitude: Double,
        newAddress: Address
    ) {
        updateLocation(latitude, longitude)
        this.address = newAddress
    }

    fun updatePeriod(startAt: LocalDateTime?, endAt: LocalDateTime?) {
        if (startAt != null && endAt != null) {
            require(startAt.isBefore(endAt)) { "시작일은 종료일보다 이전이어야 합니다" }
        }
        this.startAt = startAt
        this.endAt = endAt
    }

    companion object {
        fun create(
            id: UUID? = null,
            groupId: UUID,
            writerId: UUID,
            title: String,
            latitude: Double,
            longitude: Double,
            address: Address,
            content: String? = null,
            imageUrls: List<String>,
            parentPlaceId: UUID? = null,
            startAt: LocalDateTime? = null,
            endAt: LocalDateTime? = null
        ): Place {
            require(title.isNotBlank()) { "제목은 필수입니다" }
            require(title.length <= 30) { "제목은 30자 이하여야 합니다" }
            require(latitude in -90.0..90.0) { "위도는 -90 ~ 90 범위여야 합니다" }
            require(longitude in -180.0..180.0) { "경도는 -180 ~ 180 범위여야 합니다" }

            if (startAt != null && endAt != null) {
                require(startAt.isBefore(endAt)) { "시작일은 종료일보다 이전이어야 합니다" }
            }

            val generatedId = id ?: UuidV7Generator.generate()
            return Place(
                generatedId,
                groupId,
                writerId,
                title,
                content,
                latitude,
                longitude,
                address,
                imageUrls
            ).apply {
                this.content = content
                this.parentPlaceId = parentPlaceId
                this.startAt = startAt
                this.endAt = endAt
            }
        }
    }
}