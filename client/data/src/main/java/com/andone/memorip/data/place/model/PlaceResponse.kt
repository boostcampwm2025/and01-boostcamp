package com.andone.memorip.data.place.model

import com.andone.memorip.domain.model.Tag
import com.andone.memorip.domain.model.request.Address
import com.andone.memorip.domain.model.response.GroupCompact
import com.andone.memorip.domain.model.response.PlaceDetail
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailResponse(
    val placeId: String,
    val writerId: String,
    val title: String,
    val content: String?,
    val latitude: Double,
    val longitude: Double,
    val tags: List<Tag>,
    val images: List<String>,
    val groups: List<GroupCompactResponse>,
    val address: Address,
    val isMine: Boolean,
    val isInMyGroup: Boolean,
    val isPublic: Boolean
)

@Serializable
data class GroupCompactResponse(
    val groupId: String,
    val groupName: String
)

fun PlaceDetailResponse.toDomain(): PlaceDetail = PlaceDetail(
    placeId = this.placeId,
    writerId = this.writerId,
    title = this.title,
    content = this.content,
    latitude = this.latitude,
    longitude = this.longitude,
    tags = this.tags,
    images = this.images,
    groups = this.groups.map { it.toDomain() },
    address = this.address,
    isMine = this.isMine,
    isInMyGroup = this.isInMyGroup,
    isPublic = this.isPublic
)

fun GroupCompactResponse.toDomain(): GroupCompact = GroupCompact(
    groupId = this.groupId,
    groupName = this.groupName
)
