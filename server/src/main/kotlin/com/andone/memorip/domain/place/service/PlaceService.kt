package com.andone.memorip.domain.place.service

import com.andone.memorip.domain.group.repository.GroupRepository
import com.andone.memorip.domain.place.controller.CreatePlaceRequestDto
import com.andone.memorip.domain.place.dto.PlaceDetailResponse
import com.andone.memorip.domain.place.entity.Address
import com.andone.memorip.domain.place.entity.Place
import com.andone.memorip.domain.place.repository.PlaceRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlaceService(
    private val placeRepository: PlaceRepository,
    private val groupRepository: GroupRepository
) {
    fun getPlaceById(placeId: UUID): PlaceDetailResponse {
        val place = placeRepository.findPlaceById(id = placeId) ?: throw NoSuchElementException("Place Not Found")
        val group = groupRepository.findGroupById(id = place.groupId) ?: throw NoSuchElementException("Group Not Found")

        return PlaceDetailResponse(
            placeId = place.id ?: throw IllegalArgumentException("Place ID is NULL"),
            writerId = place.writerId,
            title = place.title,
            content = place.content,
            latitude = place.latitude,
            longitude = place.longitude,
            group = group,
            address = place.address
        )
    }

    fun createPlace(requestDto: CreatePlaceRequestDto): Place {
        val address = Address.create(
            region1Depth = requestDto.region1Depth,
            region2Depth = requestDto.region2Depth,
            region3Depth = requestDto.region3Depth,
            fullAddress = "${requestDto.region1Depth} ${requestDto.region2Depth} ${requestDto.region3Depth}"
        )
        val newPlace = Place.create(
            id = UUID.randomUUID(),
            groupId = UUID.randomUUID(),
            writerId = requestDto.writerId,
            title = requestDto.title,
            content = requestDto.content,
            latitude = requestDto.latitude,
            longitude = requestDto.longitude,
            address = address
        )
        val result = placeRepository.save(newPlace)
        return result
    }
}