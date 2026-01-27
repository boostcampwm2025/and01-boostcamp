package com.andone.memorip.feature.place.service

import com.andone.memorip.common.exception.BusinessException
import com.andone.memorip.common.exception.CommonExceptionCode
import com.andone.memorip.feature.group.dto.request.GroupPlaceCreateRequest
import com.andone.memorip.feature.group.repository.GroupRepository
import com.andone.memorip.feature.place.dto.request.PlaceGroupsUpdateRequest
import com.andone.memorip.feature.place.entity.GroupPlace
import com.andone.memorip.feature.place.repository.GroupPlaceRepository
import com.andone.memorip.feature.place.repository.PlaceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GroupPlaceService(
    private val groupRepository: GroupRepository,
    private val placeRepository: PlaceRepository,
    private val groupPlaceRepository: GroupPlaceRepository
) {
    @Transactional
    fun addPlaceToGroup(
        groupId: UUID,
        request: GroupPlaceCreateRequest
    ) {
        val group = groupRepository.findByIdOrNull(groupId)
            ?: throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)

        val place = placeRepository.findByIdOrNull(request.placeId)
            ?: throw BusinessException(code = CommonExceptionCode.PLACE_NOT_FOUND)

        val groupPlace = GroupPlace.create(
            group = group,
            place = place
        )

        groupPlaceRepository.save(groupPlace)
    }

    @Transactional
    fun updatePlaceGroups(placeId: UUID, request: PlaceGroupsUpdateRequest) {
        val place = placeRepository.findByIdOrNull(placeId)
            ?: throw BusinessException(code = CommonExceptionCode.PLACE_NOT_FOUND)

        // 추가
        if (request.addGroupIds.isNotEmpty()) {
            val existingGroupPlaces = groupPlaceRepository.findByGroupIdsAndPlaceId(
                request.addGroupIds, placeId
            ).map { it.group.id }.toSet()

            val groupIdsToAdd = request.addGroupIds.filter { it !in existingGroupPlaces }
            if (groupIdsToAdd.isNotEmpty()) {
                val groups = groupRepository.findAllById(groupIdsToAdd)
                if (groups.size != groupIdsToAdd.size) {
                    throw BusinessException(code = CommonExceptionCode.GROUP_NOT_FOUND)
                }

                val newGroupPlaces = groups.map { group ->
                    GroupPlace.create(group = group, place = place)
                }
                groupPlaceRepository.saveAll(newGroupPlaces)
            }
        }

        // 제거
        if (request.removeGroupIds.isNotEmpty()) {
            groupPlaceRepository.deleteByGroupIdsAndPlaceId(request.removeGroupIds, placeId)
        }
    }
}